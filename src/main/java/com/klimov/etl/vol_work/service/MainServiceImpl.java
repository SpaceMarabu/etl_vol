package com.klimov.etl.vol_work.service;

import com.klimov.etl.vol_work.dao.UserRepository;
import com.klimov.etl.vol_work.dao.entity.UserTaskDBModel;
import com.klimov.etl.vol_work.dao.mapper.DBMapper;
import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.dto.exceptions.DagNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.DagRunNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;
import com.klimov.etl.vol_work.dto.mapper.ApiMapper;
import com.klimov.etl.vol_work.dto.repository.DagRepository;
import com.klimov.etl.vol_work.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    private final UserRepository userRepository;
    private final DagRepository dagRepository;
    private final DBMapper dbMapper;
    private final ApiMapper apiMapper;
    private final MainScreenStateService userState;


    public MainServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired DagRepository dagRepository,
                           @Autowired DBMapper dbMapper,
                           @Autowired ApiMapper apiMapper) {

        this.userRepository = userRepository;
        this.dbMapper = dbMapper;
        this.dagRepository = dagRepository;
        this.apiMapper = apiMapper;
        this.userState = new MainScreenStateService();

    }

    @Override
    @Transactional
    public List<UserTask> getAllUsers() {

        List<UserTask> userList = new ArrayList<>();

        for (UserTaskDBModel userTaskDBModel : userRepository.getAllUsersTasks()) {
            userList.add(
                    dbMapper.getUserTaskFromDBModel(userTaskDBModel)
            );
        }

        return userList;
    }

    @Transactional
    public void saveUserTasks() {
        if (userState.isSignedIn()) {

            List<UserTask> userTasks = userState.getUserTaskList();
            List<UserTaskDBModel> userTaskDBModels = new ArrayList<>();

            for (UserTask userTask : userTasks) {
                userTaskDBModels.add(
                        dbMapper.getUserTaskDBModelFromEntity(userTask)
                );
            }

            userRepository.saveUserTasks(userTaskDBModels);
        }
    }

    @Override
    public MainScreenStateService getUserState() {
        return userState;
    }

    @Async
    @Transactional
    @Override
    public void failDag(DagRunUI dagRun) throws IOException, URISyntaxException, InterruptedException {

        synchronized (this.userState) {

            MainScreenStateService tempUserState = this.userState;

            DagRunInfoDto dagRunInfoDto = dagRepository.failDag(dagRun.getDagId(), dagRun.getDagRunId());
            DagRunUI returnedDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

            for (DagRunUI dagRunFromList : tempUserState.getDagRunList()) {
                if (dagRunFromList.getDagId().equals(returnedDagRun.getDagId())) {
                    dagRunFromList.setState(returnedDagRun.getState());
                }
                break;
            }

            for (UserTask userTask : tempUserState.getUserTaskList()) {
                if (userTask.getDagId().equals(returnedDagRun.getDagId())) {
                    userTask.setPause(true);
                }
                break;
            }

            updateUserState(tempUserState);
        }

        loadUserState();
    }

    @Scheduled(fixedRate = 60000)
    @Async("singleThreadExecutor")//большая основная загрузка, так что задачи поочередно
    @Override
    @Transactional
    public void loadUserState()
            throws IOException, URISyntaxException, InterruptedException,
            DagRunNotFoundException, DagNotFoundException {

        System.out.println("=================Начато обновление userState=================");

        if (this.userState.isSignedIn()) {
            synchronized (this.userState) {

                MainScreenStateService tempUserState = this.userState;
                List<DagRunUI> observes = new ArrayList<>();
                List<DagRunUI> dagRuns = new ArrayList<>();

                //начинаю обход задач
                System.out.println(LocalDateTime.now() + " : начинаю обход задач и наполняю лист запусков");
                for (UserTask userTask : tempUserState.getUserTaskList()) {
                    //если с таской связан runId
                    if (userTask.getLastRunId() != null && !isObserveTask(userTask)) {
                        System.out.println(LocalDateTime.now() + " : Указана связь с запуском. Обновляю инфу");
                        //получаю инфу по этому запуску
                        DagRunInfoDto dagRunInfoDto =
                                dagRepository.getDagRunInfo(userTask.getDagId(), userTask.getLastRunId());
                        DagRunUI dagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                        linkTaskToDagRun(dagRun, userTask);
                        dagRuns.add(dagRun);
                        System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() + ": " +
                                dagRun.getState() + ". " + "insert list");

                    } else {
                        //если с таской не связан runId или это observe
                        System.out.println(LocalDateTime.now() + " : Связи с запуском нет. Ищу");
                        DagRunUI dagRun;

                        try {
                            DagRunInfoDto dagRunInfoDto = dagRepository.getLastDagRunInfo(userTask.getDagId());
                            dagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);
                            userTask.setLastRunId(dagRun.getDagRunId());
                            System.out.println(LocalDateTime.now() + " : Найден запуск " + dagRun.getDagRunId());
                        } catch (DagRunNotFoundException e) {
                            dagRun = new DagRunUI();
                            dagRun.setState("no runs");
                            dagRun.setDagId(userTask.getDagId());
                            System.out.println(LocalDateTime.now() + " : Запусков ранее не было");
                        }

                        if (isObserveTask(userTask)) {
                            observes.add(dagRun);
                        } else {
                            dagRuns.add(dagRun);
                        }
                        linkTaskToDagRun(dagRun, userTask);
                    }
                }

                tempUserState.setDagRunList(dagRuns);
                tempUserState.setDagObserveList(observes);

                if (!userState.isPause()) {
                    System.out.println(LocalDateTime.now() + " : Начинаю обход запусков");
                    for (DagRunUI dagRun : tempUserState.getDagRunList()) {

                        switch (dagRun.getState()) {
                            case "failed" -> {
                                System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() + " упал");

                                for (UserTask userTask : tempUserState.getUserTaskList()) {
                                    if (userTask.getDagId().equals(dagRun.getDagId()) && !userTask.isPause()) {
                                        userTask.incrementError();
                                        if (userTask.getCountErrors() <= 5) {
                                            System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() +
                                                    " упал менее пяти раз. Перезапуск");

                                            try {
                                                DagRunInfoDto dagRunInfoDto =
                                                        dagRepository.triggerDag(dagRun.getDagId(), dagRun.getConf());
                                                DagRunUI newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                                                userTask.setLastRunId(newDagRun.getDagRunId());
                                                changeDagRunState(dagRun, newDagRun);
                                            } catch (DagRunNotFoundException e) {
                                                dagRun.setState("backend error");
                                            }

                                        } else {
                                            dagRun.setState("error");
                                        }

                                        break;
                                    }
                                }
                            }
                            case "success" -> {
                                System.out.print(dagRun.getDagId() + " успешно отбежал.");
                                for (UserTask userTask : tempUserState.getUserTaskList()) {
                                    if (userTask.getDagId().equals(dagRun.getDagId())) {

                                        if (isObserveTask(userTask) || userTask.isPause()) break;

                                        if (userTask.getListConf().isEmpty()) {
                                            dagRun.setState("done");
                                            System.out.println(" : и конфигов больше нет");
                                        } else {

                                            if (dagRun.getConf().trim().equals(
                                                    userTask.getListConf().get(0).trim()
                                            )) {
                                                System.out.println(" : и с нужным конфигом");
                                                userTask.removeFirstConfig();
                                            }

                                            if (!dagRun.getConf().isEmpty()) {
                                                tryTriggerDag(dagRun, userTask);
                                            }
                                        }

                                        break;
                                    }
                                }
                            }
                            case "no runs" -> {
                                System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() + " ранее никогда не бегал.");
                                for (UserTask userTask : tempUserState.getUserTaskList()) {
                                    if (userTask.getDagId().equals(dagRun.getDagId())) {

                                        if (isObserveTask(userTask) || userTask.isPause()) break;

                                        System.out.println(LocalDateTime.now() + " : Запускаю");

                                        tryTriggerDag(dagRun, userTask);

                                        break;
                                    }

                                }
                            }
                        }

                    }
                } else {
                    System.out.println(LocalDateTime.now() + " : Пауза запусков");
                }

                tempUserState.initDone();
                updateUserState(tempUserState);
                System.out.println(LocalDateTime.now() + " : Обновление userState завершено");
                saveUserTasks();

            }
        }
    }

    private void tryTriggerDag(DagRunUI dagRun, UserTask userTask) throws IOException, InterruptedException, URISyntaxException {
        try {
            DagRunInfoDto dagRunInfoDto =
                    dagRepository.triggerDag(dagRun.getDagId(), userTask.getListConf().get(0));
            DagRunUI newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

            userTask.setLastRunId(newDagRun.getDagRunId());
            changeDagRunState(dagRun, newDagRun);
        } catch (DagRunNotFoundException e) {
            dagRun.setState("backend error");
        }
    }

    @Override
    @Transactional
    public void signIn(String login, String password)
            throws UnauthorizedException, URISyntaxException, IOException, InterruptedException {

        dagRepository.checkAccess(login, password);

        synchronized (this.userState) {
            this.userState.setSignedIn(true);
            this.userState.setUserId(login);

            List<UserTaskDBModel> userTasksModel = userRepository.getUserTasks(login);
            System.out.println(userTasksModel);
            List<UserTask> userTaskList = new ArrayList<>();

            for (UserTaskDBModel userTaskDBModel : userTasksModel) {
                userTaskList.add(
                        dbMapper.getUserTaskFromDBModel(userTaskDBModel)
                );
            }

            this.userState.setUserTaskList(userTaskList);
        }

        loadUserState();
    }

    @Override
    @Transactional
    public void addTask(UserTaskFromUI userTaskRaw) throws IOException, URISyntaxException, InterruptedException {

        if (//проверяю нет ли уже в списке такого потока
                !this.userState.getUserTaskList().stream().filter(
                        userTask -> userTask.getDagId().equals(userTaskRaw.getDagId())
                ).toList().isEmpty()
        ) {
            return;
        }

        List<String> listConf;
        if (userTaskRaw.getListConfRaw() != null && !userTaskRaw.getListConfRaw().isEmpty()) {
            listConf = Arrays.stream(
                    userTaskRaw.getListConfRaw().split("\\n")
            ).toList();
        } else {
            listConf = new ArrayList<>();
            listConf.add("{}");
        }

        UserTask userTask = new UserTask(
                this.userState.getUserId(),
                userTaskRaw.getDagId(),
                userTaskRaw.getRunType(),
                userTaskRaw.getTaskId(),
                listConf,
                userTaskRaw.getComment(),
                0
        );
        userTask.setComment(userTaskRaw.getComment());

        synchronized (this.userState) {
            this.userState.getUserTaskList().add(userTask);
        }

        loadUserState();
    }

    @Override
    @Transactional
    public void deleteTask(String taskId) throws IOException, URISyntaxException, InterruptedException {
        synchronized (this.userState) {

            List<UserTask> userTaskList = this.userState.getUserTaskList();

            for (int i = 0; i < userTaskList.size(); i++) {
                UserTask userTask = userTaskList.get(i);
                if (userTask.getTaskId().equals(taskId)) {
                    userRepository.deleteUserTask(this.userState.getUserId(), userTask.getDagId());
                    userTaskList.remove(i);
                    break;
                }
            }
        }

        loadUserState();
    }

    @Override
    public void resetTask(String taskId) {
        synchronized (this.userState) {
            for (UserTask userTask : this.userState.getUserTaskList()) {
                if (userTask.getTaskId().equals(taskId)) {
                    userTask.resetErrors();
                }
            }
        }
    }

    @Override
    public void pauseStarts() {
        synchronized (this.userState) {
            this.userState.setPause(true);
        }
    }

    @Override
    public void unpauseStarts() {
        synchronized (this.userState) {
            this.userState.setPause(false);
        }
    }

    private boolean isObserveTask(UserTask userTask) {
        return userTask.getRunType() == RunType.OBSERVE;
    }

    private void updateUserState(MainScreenStateService newUserState) {
        userState.setUserTaskList(newUserState.getUserTaskList());
        userState.setDagRunList(newUserState.getDagRunList());
        userState.setDagObserveList(newUserState.getDagObserveList());
    }

    private void changeDagRunState(DagRunUI oldDR, DagRunUI newDR) {
        oldDR.setState(newDR.getState());
        oldDR.setStartDate(newDR.getStartDate());
        oldDR.setDagRunId(newDR.getDagRunId());
    }

    private void linkTaskToDagRun(DagRunUI dr, UserTask ut) {
        dr.setComment(ut.getComment());
        dr.setTaskId(ut.getTaskId());
    }
}

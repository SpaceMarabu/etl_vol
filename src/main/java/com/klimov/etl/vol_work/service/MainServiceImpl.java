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
import com.klimov.etl.vol_work.entity.DagRunUI;
import com.klimov.etl.vol_work.entity.MainScreenState;
import com.klimov.etl.vol_work.entity.UserTask;
import com.klimov.etl.vol_work.entity.UserTaskFromUI;
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
import java.util.stream.Collectors;

@Service
public class MainServiceImpl implements MainService {

    private final UserRepository userRepository;
    private final DagRepository dagRepository;
    private final DBMapper dBmapper;
    private final ApiMapper apiMapper;
    private final MainScreenState userState;


    public MainServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired DagRepository dagRepository,
                           @Autowired DBMapper dbMapper,
                           @Autowired ApiMapper apiMapper) {

        this.userRepository = userRepository;
        this.dBmapper = dbMapper;
        this.dagRepository = dagRepository;
        this.apiMapper = apiMapper;
        this.userState = new MainScreenState();

    }

    @Override
    @Transactional
    public List<UserTask> getAllUsers() {

        List<UserTask> userList = new ArrayList<>();

        for (UserTaskDBModel userTaskDBModel : userRepository.getAllUsersTasks()) {
            userList.add(
                    dBmapper.getUserTaskFromDBModel(userTaskDBModel)
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
                        dBmapper.getUserTaskDBModelFromEntity(userTask)
                );
            }

            userRepository.saveUserTasks(userTaskDBModels);
        }
    }

    @Override
    public MainScreenState getUserState() {
        return userState;
    }

    @Async
    @Transactional
    @Override
    public void failDag(DagRunUI dagRun) throws IOException, URISyntaxException, InterruptedException {

        synchronized (this.userState) {

            MainScreenState tempUserState = this.userState;

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

    @Scheduled(fixedRate = 6000)
    @Async
    @Override
    @Transactional
    public void loadUserState()
            throws IOException, URISyntaxException, InterruptedException,
            DagRunNotFoundException, DagNotFoundException {


        System.out.println("=================Начато обновление userState=================");

        if (this.userState.isSignedIn()) {
            synchronized (this.userState) {
                MainScreenState tempUserState = this.userState;

                List<DagRunUI> newDagRuns = new ArrayList<>();
                //если запуски были ранее, то берем те, у которых есть runId. Если его нет, то удаляем - наполнение ниже
                List<DagRunUI> oldDagRuns = tempUserState.getDagRunList() != null
                        ? tempUserState.getDagRunList()
                        .stream()
                        .filter(dagRun -> dagRun.getDagRunId() != null)
                        .collect(Collectors.toList()) :
                        new ArrayList<>();

                //начинаю обход задач
                System.out.println(LocalDateTime.now() + " : начинаю обход задач и наполняю лист запусков");
                for (UserTask userTask : tempUserState.getUserTaskList()) {
                    //если с таской связан runId
                    if (userTask.getLastRunId() != null) {
                        System.out.println(LocalDateTime.now() + " : Указана связь с запуском. Обновляю инфу");
                        //получаю инфу по этому запуску
                        DagRunInfoDto dagRunInfoDto =
                                dagRepository.getDagRunInfo(userTask.getDagId(), userTask.getLastRunId());
                        DagRunUI dagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                        linkTaskToDagRun(dagRun, userTask);

                        //правлю существующий dagRun или добавляю новый
                        boolean isDagRunExists = false;
                        for (DagRunUI runFromUserState : tempUserState.getDagRunList()) {
                            if (runFromUserState.getDagRunId() != null &&
                                    runFromUserState.getDagRunId().equals(userTask.getLastRunId())) {
                                System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() + ": " +
                                        dagRun.getState() + ". " + "update list");
                                runFromUserState.setState(dagRun.getState());
                                isDagRunExists = true;
                                break;
                            }
                        }

                        if (!isDagRunExists) {
                            newDagRuns.add(dagRun);
                            System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() + ": " +
                                    dagRun.getState() + ". " + "insert list");
                        }

                    } else {
                        //если с таской не связан runId
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

                        linkTaskToDagRun(dagRun, userTask);
                        newDagRuns.add(dagRun);
                    }
                }

                oldDagRuns.addAll(newDagRuns);
                tempUserState.setDagRunList(oldDagRuns);

                System.out.println(LocalDateTime.now() + " : Начинаю обход запусков");
                for (DagRunUI dagRun : tempUserState.getDagRunList()) {

                    if (dagRun.getState().equals("failed")) {
                        System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() + " упал");

                        for (UserTask userTask : tempUserState.getUserTaskList()) {
                            if (userTask.getDagId().equals(dagRun.getDagId()) && !userTask.isPause()) {
                                userTask.incrementError();
                                if (userTask.getCountErrors() <= 5) {
                                    System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() +
                                            " упал менее пяти раз. Перезапуск");
                                    DagRunInfoDto dagRunInfoDto =
                                            dagRepository.triggerDag(dagRun.getDagId(), dagRun.getConf());
                                    DagRunUI newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                                    userTask.setLastRunId(newDagRun.getDagRunId());
                                    changeDagRunState(dagRun, newDagRun);
                                }
                                break;
                            }
                        }
                    } else if (dagRun.getState().equals("success")) {
                        System.out.print(dagRun.getDagId() + " успешно отбежал.");
                        for (UserTask userTask : tempUserState.getUserTaskList()) {
                            if (userTask.getDagId().equals(dagRun.getDagId()) && !userTask.isPause()) {
                                if (userTask.getListConf().size() == 1) {
                                    userTask.done();
                                    System.out.println(" : и конфигов больше нет");
                                } else {


                                    if (dagRun.getConf().trim().equals(
                                            userTask.getListConf().get(0).trim()
                                    )) {
                                        System.out.println(" : и с нужным конфигом");
                                        userTask.removeFirstConfig();
                                    }

                                    DagRunInfoDto dagRunInfoDto =
                                            dagRepository.triggerDag(dagRun.getDagId(), userTask.getListConf().get(0));
                                    DagRunUI newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                                    userTask.setLastRunId(newDagRun.getDagRunId());
                                    changeDagRunState(dagRun, newDagRun);

                                }

                                break;
                            }
                        }
                    } else if (dagRun.getState().equals("no runs")) {
                        System.out.println(LocalDateTime.now() + " : " + dagRun.getDagId() + " ранее никогда не бегал.");
                        for (UserTask userTask : tempUserState.getUserTaskList()) {
                            if (userTask.getDagId().equals(dagRun.getDagId()) && !userTask.isPause()) {
                                System.out.println(LocalDateTime.now() + " : Запускаю");
                                DagRunInfoDto dagRunInfoDto =
                                        dagRepository.triggerDag(dagRun.getDagId(), userTask.getListConf().get(0));
                                DagRunUI newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                                userTask.setLastRunId(newDagRun.getDagRunId());
                                changeDagRunState(dagRun, newDagRun);

                                break;
                            }

                        }
                    }

                }

                updateUserState(tempUserState);
                System.out.println(LocalDateTime.now() + " : Обновление userState завершено");
                saveUserTasks();

            }
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
            List<UserTask> userTaskList = new ArrayList<>();

            for (UserTaskDBModel userTaskDBModel : userTasksModel) {
                userTaskList.add(
                        dBmapper.getUserTaskFromDBModel(userTaskDBModel)
                );
            }

            this.userState.setUserTaskList(userTaskList);

        }
    }

    @Override
    @Transactional
    public void addTask(UserTaskFromUI userTaskRAW) throws IOException, URISyntaxException, InterruptedException {

        List<String> listConf;
        if (userTaskRAW.getListConfRAW() != null && !userTaskRAW.getListConfRAW().isEmpty()) {
            listConf = Arrays.stream(
                    userTaskRAW.getListConfRAW().split("\\n")
            ).toList();
        } else {
            listConf = new ArrayList<>();
            listConf.add("{}");
        }

        UserTask userTask = new UserTask(
                this.userState.getUserId(),
                userTaskRAW.getDagId(),
                userTaskRAW.getRunType(),
                userTaskRAW.getTaskId(),
                listConf,
                userTaskRAW.getComment(),
                0
        );
        userTask.setComment(userTaskRAW.getComment());

        synchronized (this.userState) {
            this.userState.getUserTaskList().add(userTask);
        }

        loadUserState();
    }

    private void updateUserState(MainScreenState newUserState) {
        userState.setUserTaskList(newUserState.getUserTaskList());
        userState.setDagRunList(newUserState.getDagRunList());
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

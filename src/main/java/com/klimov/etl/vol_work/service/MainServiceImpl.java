package com.klimov.etl.vol_work.service;

import com.klimov.etl.vol_work.dao.UserRepository;
import com.klimov.etl.vol_work.dao.entity.UserDBModel;
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
    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();

        for (UserDBModel userDBModel : userRepository.getAllUsers()) {
            userList.add(
                    dBmapper.getUserFromDBModel(userDBModel)
            );
        }

        return userList;
    }

    @Scheduled(fixedRate = 6000)
    @Transactional
    public void saveUser() {
        if (userState.isSignedIn()) {
            userRepository.saveUser(
                    dBmapper.getUserDBModelFromEntity(userState.getUser())
            );
        }
    }

    @Override
    public MainScreenState getUserState() {
        return userState;
    }

    @Async
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

            for (UserTask userTask : tempUserState.getUser().getListTasks()) {
                if (userTask.getDagId().equals(returnedDagRun.getDagId())) {
                    userTask.setPause(true);
                }
                break;
            }

            updateUserState(tempUserState);

        }
    }

    @Scheduled(fixedRate = 6000)
    @Async
    @Override
    @Transactional
    public void loadUserState()
            throws IOException, URISyntaxException, InterruptedException,
            DagRunNotFoundException, DagNotFoundException {

        System.out.println("Начато обновление userState");

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
                for (UserTask userTask : tempUserState.getUser().getListTasks()) {
                    //если с таской связан runId
                    if (userTask.getLastRunId() != null) {

                        //получаю инфу по этому запуску
                        DagRunInfoDto dagRunInfoDto =
                                dagRepository.getDagRunInfo(userTask.getDagId(), userTask.getLastRunId());
                        DagRunUI dagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                        linkTaskToDagRun(dagRun, userTask);

                        //правлю существующий dagRun или добавляю новый
                        boolean isDagRunExists = false;
                        for (DagRunUI runFromUserState : tempUserState.getDagRunList()) {
                            if (runFromUserState.getDagRunId().equals(userTask.getLastRunId())) {
                                runFromUserState.setState(dagRun.getState());
                                isDagRunExists = true;
                                break;
                            }
                        }

                        if (!isDagRunExists) {
                            newDagRuns.add(dagRun);
                        }

                    } else {
                        //если с таской не связан runId
                        DagRunUI dagRun;

                        try {
                            DagRunInfoDto dagRunInfoDto = dagRepository.getLastDagRunInfo(userTask.getDagId());
                            dagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);
                            userTask.setLastRunId(dagRun.getDagRunId());
                        } catch (DagRunNotFoundException e) {
                            dagRun = new DagRunUI();
                            dagRun.setState("no runs");
                            dagRun.setDagId(userTask.getDagId());
                        }

                        linkTaskToDagRun(dagRun, userTask);
                        newDagRuns.add(dagRun);
                    }
                }

                oldDagRuns.addAll(newDagRuns);
                tempUserState.setDagRunList(oldDagRuns);

                for (DagRunUI dagRun : tempUserState.getDagRunList()) {

                    if (dagRun.getState().equals("failed")) {
                        for (UserTask userTask : tempUserState.getUser().getListTasks()) {
                            if (userTask.getDagId().equals(dagRun.getDagId()) && !userTask.isPause()) {
                                userTask.incrementError();
                                if (userTask.getCountErrors() <= 5) {
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
                        for (UserTask userTask : tempUserState.getUser().getListTasks()) {
                            if (userTask.getDagId().equals(dagRun.getDagId()) && !userTask.isPause()) {
                                if (userTask.getListConf().size() == 1) {
                                    userTask.done();
                                } else {
                                    userTask.getListConf().remove(0);

                                    DagRunInfoDto dagRunInfoDto =
                                            dagRepository.triggerDag(dagRun.getDagId(), userTask.getListConf().get(0));
                                    DagRunUI newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                                    userTask.setLastRunId(newDagRun.getDagRunId());
                                    changeDagRunState(dagRun, newDagRun);

                                }
                                break;
                            }
                        }
                    }
                }

                updateUserState(tempUserState);
                System.out.println("Обновление userState завершено");

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

            User newUser = new User(login);
            UserDBModel userModel = userRepository.getUser(login);

            if (userModel == null) {
                this.userState.setUser(newUser);
            } else if (!userModel.getListTasks().isEmpty()) {
                User userModelMapped = dBmapper.getUserFromDBModel(userModel);
                newUser.setListTasks(userModelMapped.getListTasks());
            }

            this.userState.setUser(newUser);

        }
    }

    @Override
    @Transactional
    public void addTask(UserTaskFromUI userTaskRAW) throws IOException, URISyntaxException, InterruptedException {
        UserDBModel userModel = userRepository.getUser(userState.getUser().getUserId());
        User user = dBmapper.getUserFromDBModel(userModel);

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
                user.getUserId(),
                userTaskRAW.getDagId(),
                userTaskRAW.getRunType(),
                userTaskRAW.getTaskId(),
                listConf
        );
        userTask.setComment(userTaskRAW.getComment());

        user.addTask(userTask);

        synchronized (this.userState) {
            this.userState.setUser(user);
        }

        loadUserState();
    }

    private void updateUserState(MainScreenState newUserState) {
        userState.setUser(newUserState.getUser());
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

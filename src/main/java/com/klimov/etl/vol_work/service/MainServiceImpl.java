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
import com.klimov.etl.vol_work.entity.DagRun;
import com.klimov.etl.vol_work.entity.User;
import com.klimov.etl.vol_work.entity.MainScreenState;
import com.klimov.etl.vol_work.entity.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    @Override
    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();

        for (UserDBModel userDBModel : userRepository.getAllUsers()) {
            userList.add(
                    dBmapper.getUserFromDBModel(userDBModel)
            );
        }

        return userList;
    }

    @Scheduled(fixedRate = 600000)
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
    public void failDag(DagRun dagRun) throws IOException, URISyntaxException, InterruptedException {

        synchronized (this.userState) {

            MainScreenState tempUserState = this.userState;

            DagRunInfoDto dagRunInfoDto = dagRepository.failDag(dagRun.getDagId(), dagRun.getDagRunId());
            DagRun returnedDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

            for (DagRun dagRunFromList : tempUserState.getDagRunList()) {
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

    @Scheduled(fixedRate = 60000)
    @Async
    @Override
    public void loadUserState()
            throws IOException, URISyntaxException, InterruptedException,
            DagRunNotFoundException, DagNotFoundException {

        if (this.userState.isSignedIn()) {
            synchronized (this.userState) {
                MainScreenState tempUserState = this.userState;

                List<DagRun> newDagRuns = new ArrayList<>();
                List<DagRun> oldDagRuns = tempUserState.getDagRunList() != null
                        ? tempUserState.getDagRunList() : new ArrayList<>();

                for (UserTask userTask : tempUserState.getUser().getListTasks()) {
                    if (userTask.getLastRunId() != null) {

                        DagRunInfoDto dagRunInfoDto =
                                dagRepository.getDagRunInfo(userTask.getDagId(), userTask.getLastRunId());
                        DagRun dagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                        userTask.setLastRunId(dagRun.getDagRunId());

                        for (DagRun runFromUserState : tempUserState.getDagRunList()) {
                            if (runFromUserState.getDagRunId().equals(userTask.getLastRunId())) {
                                runFromUserState.setState(dagRun.getState());
                                break;
                            }
                        }

                    } else {

                        DagRunInfoDto dagRunInfoDto = dagRepository.getLastDagRunInfo(userTask.getDagId());
                        DagRun dagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                        userTask.setLastRunId(dagRun.getDagRunId());

                        newDagRuns.add(dagRun);
                    }
                }

                oldDagRuns.addAll(newDagRuns);
                userState.setDagRunList(oldDagRuns);

                for (DagRun dagRun : tempUserState.getDagRunList()) {

                    if (dagRun.getState().equals("failed")) {
                        for (UserTask userTask : tempUserState.getUser().getListTasks()) {
                            if (userTask.getDagId().equals(dagRun.getDagId()) && !userTask.isPause()) {
                                userTask.incrementError();
                                if (userTask.getCountErrors() <= 5) {
                                    DagRunInfoDto dagRunInfoDto =
                                            dagRepository.triggerDag(dagRun.getDagId(), dagRun.getConf());
                                    DagRun newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                                    userTask.setLastRunId(newDagRun.getDagRunId());
                                    dagRun.setState(newDagRun.getState());
                                    dagRun.setStartDate(newDagRun.getStartDate());
                                    dagRun.setDagRunId(newDagRun.getDagRunId());
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
                                    DagRun newDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

                                    userTask.setLastRunId(newDagRun.getDagRunId());
                                    dagRun.setState(newDagRun.getState());
                                    dagRun.setStartDate(newDagRun.getStartDate());
                                    dagRun.setDagRunId(newDagRun.getDagRunId());
                                }
                                break;
                            }
                        }
                    }
                }

                updateUserState(tempUserState);
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
                userRepository.saveUser(dBmapper.getUserDBModelFromEntity(newUser));
            } else if (!userModel.getListTasks().isEmpty()) {
                User userModelMapped = dBmapper.getUserFromDBModel(userModel);
                newUser.setListTasks(userModelMapped.getListTasks());
            }

            this.userState.setUser(newUser);

        }
    }


    private void updateUserState(MainScreenState newUserState) {
        userState.setUser(newUserState.getUser());
        userState.setDagRunList(newUserState.getDagRunList());
    }
}

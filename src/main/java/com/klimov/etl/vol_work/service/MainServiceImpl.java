package com.klimov.etl.vol_work.service;

import com.klimov.etl.vol_work.dao.UserRepository;
import com.klimov.etl.vol_work.dao.entity.UserDBModel;
import com.klimov.etl.vol_work.dao.mapper.DBMapper;
import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.dto.mapper.ApiMapper;
import com.klimov.etl.vol_work.dto.repository.DagRepository;
import com.klimov.etl.vol_work.entity.DagRun;
import com.klimov.etl.vol_work.entity.User;
import com.klimov.etl.vol_work.entity.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private final UserState userState;


    public MainServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired DagRepository dagRepository,
                           @Autowired DBMapper dbMapper,
                           @Autowired ApiMapper apiMapper) {

        this.userRepository = userRepository;
        this.dBmapper = dbMapper;
        this.dagRepository = dagRepository;
        this.apiMapper = apiMapper;
        this.userState = new UserState();

    }

    @Transactional
    @Override
    public User getUser(String userId) {
        return dBmapper.getUserFromDBModel(
                userRepository.getUser(userId)
        );
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

    @Transactional
    @Override
    public void saveUser(User user) {
        userRepository.saveUser(
                dBmapper.getUserDBModelFromEntity(user)
        );
    }

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Async
    @Override
    public void failDag(DagRun dagRun) throws IOException, URISyntaxException, InterruptedException {

        synchronized (this.userState) {

            UserState tempUserState = this.userState;
            List<DagRun> dagRunList = tempUserState.getDagRunList();

            DagRunInfoDto dagRunInfoDto = dagRepository.failDag(
                    apiMapper.getDagRunInfoDtoFromEntity(dagRun)
            );
            DagRun returnedDagRun = apiMapper.getDagRunInfoEntityFromDto(dagRunInfoDto);

            for (int i = 0; i < dagRunList.size(); i++) {
                if (dagRunList.get(i).getDagId().equals(returnedDagRun.getDagId())
                        && dagRunList.get(i).getDagRunId().equals(returnedDagRun.getDagRunId())) {
                    dagRunList.set(i, returnedDagRun);
                }
            }

            tempUserState.setDagRunList(dagRunList);
            updateUserState(tempUserState);

        }
    }

    @Override
    public void loadUserState() {

        synchronized (this.userState) {
            UserState tempUserState = this.userState;

        }

    }


    private void updateUserState(UserState newUserState) {
        userState.setUser(newUserState.getUser());
        userState.setDagRunList(newUserState.getDagRunList());
        userState.setUserTaskList(newUserState.getUserTaskList());
    }

//    @Scheduled(fixedRate = 60000) // Раз в минуту
//    public void updateData() {
//        dataService.fetchDataAsync();
//    }
}

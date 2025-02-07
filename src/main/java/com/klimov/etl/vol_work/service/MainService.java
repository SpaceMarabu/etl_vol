package com.klimov.etl.vol_work.service;

import com.klimov.etl.vol_work.entity.DagRun;
import com.klimov.etl.vol_work.entity.User;
import com.klimov.etl.vol_work.entity.UserState;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface MainService {

    User getUser(String userId);

    List<User> getAllUsers();

    void saveUser(User user);

    UserState getUserState();

    void failDag(DagRun dagRun) throws IOException, URISyntaxException, InterruptedException;

    void loadUserState();
}

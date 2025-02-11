package com.klimov.etl.vol_work.service;

import com.klimov.etl.vol_work.entity.DagRun;
import com.klimov.etl.vol_work.entity.User;
import com.klimov.etl.vol_work.entity.UserState;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface MainService {

    List<User> getAllUsers();

    UserState getUserState();

    void failDag(DagRun dagRun) throws IOException, URISyntaxException, InterruptedException;

    void loadUserState() throws IOException, URISyntaxException, InterruptedException;

    void signIn(String login, String password) throws AuthenticationException, URISyntaxException, IOException, InterruptedException;
}

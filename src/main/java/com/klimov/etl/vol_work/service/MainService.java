package com.klimov.etl.vol_work.service;

import com.klimov.etl.vol_work.entity.*;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface MainService {

    List<UserTask> getAllUsers();

    MainScreenStateService getUserState();

    void failDag(DagRunUI dagRun) throws IOException, URISyntaxException, InterruptedException;

    void loadUserState() throws IOException, URISyntaxException, InterruptedException;

    void signIn(String login, String password) throws AuthenticationException, URISyntaxException, IOException, InterruptedException;

    void addTask(UserTaskFromUI userTask) throws IOException, URISyntaxException, InterruptedException;

    void deleteTask(String taskId) throws IOException, URISyntaxException, InterruptedException;

    void resetTask(String taskId);

    void pauseStarts();

    void unpauseStarts();
}

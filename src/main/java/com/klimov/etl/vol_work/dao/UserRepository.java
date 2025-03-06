package com.klimov.etl.vol_work.dao;

import com.klimov.etl.vol_work.dao.entity.UserTaskDBModel;

import java.util.List;

public interface UserRepository {

    List<UserTaskDBModel> getAllUsersTasks();

    void saveUserTasks(List<UserTaskDBModel> userTasks);

    void deleteUserTask(String userId, String dagId);

    List<UserTaskDBModel> getUserTasks(String userId);
}

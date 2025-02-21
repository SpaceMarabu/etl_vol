package com.klimov.etl.vol_work.dao;

import com.klimov.etl.vol_work.dao.entity.UserTaskDBModel;

import java.util.List;

public interface UserRepository {

    List<UserTaskDBModel> getAllUsersTasks();

    void saveUserTasks(List<UserTaskDBModel> userTasks);

    List<UserTaskDBModel> getUserTasks(String userId);
}

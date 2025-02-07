package com.klimov.etl.vol_work.dao;

import com.klimov.etl.vol_work.dao.entity.UserDBModel;
import com.klimov.etl.vol_work.entity.User;

import java.util.List;

public interface UserRepository {

    List<UserDBModel> getAllUsers();

    void saveUser(UserDBModel user);

    UserDBModel getUser(String userId);
}

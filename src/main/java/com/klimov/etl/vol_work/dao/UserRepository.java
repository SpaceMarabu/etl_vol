package com.klimov.etl.vol_work.dao;

import com.klimov.etl.vol_work.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUser(String userId);
}

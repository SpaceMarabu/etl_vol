package com.klimov.etl.vol_work.service;

import com.klimov.etl.vol_work.entity.User;

import java.util.List;

public interface UserService {

    User getUser(String userId);

    List<User> getAllUsers();

    void saveUser(User user);
}

package com.klimov.etl.vol_work.entity;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userId;
    private List<UserTask> listTasks;

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
        this.listTasks = new ArrayList<>();
    }

    public User(String userId, List<UserTask> listTasks) {
        this.userId = userId;
        this.listTasks = listTasks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserTask> getListTasks() {
        return listTasks;
    }

    public void setListTasks(List<UserTask> listTasks) {
        this.listTasks = listTasks;
    }
}

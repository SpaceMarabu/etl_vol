package com.klimov.etl.vol_work.dao.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class UserDBModel {

    @Id
    @Column(name = "id")
    private String userId;
    @Column(name = "listTasks")
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserTaskDBModel> listTasks;

    public UserDBModel() {
    }

    public UserDBModel(String userId, List<UserTaskDBModel> listTasks) {
        this.userId = userId;
        this.listTasks = listTasks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserTaskDBModel> getListTasks() {
        return listTasks;
    }

    public void setListTasks(List<UserTaskDBModel> listTasks) {
        this.listTasks = listTasks;
    }


}

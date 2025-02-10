package com.klimov.etl.vol_work.entity;

import java.util.List;

public class UserState {
    private User user;
    private boolean signedIn;
    private List<UserTask> userTaskList;
    private List<DagRun> dagRunList;

    public User getUser() {
        return user;
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserTask> getUserTaskList() {
        return userTaskList;
    }

    public void setUserTaskList(List<UserTask> userTaskList) {
        this.userTaskList = userTaskList;
    }

    public List<DagRun> getDagRunList() {
        return dagRunList;
    }

    public void setDagRunList(List<DagRun> dagRunList) {
        this.dagRunList = dagRunList;
    }
}

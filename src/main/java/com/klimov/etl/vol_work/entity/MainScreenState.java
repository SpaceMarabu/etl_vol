package com.klimov.etl.vol_work.entity;

import java.util.List;

public class MainScreenState {
    private User user;
    private boolean isPause;
    private boolean signedIn;
    private List<DagRun> dagRunList;
    private UserTask addingUserTask;

    public MainScreenState() {
        this.isPause = true;
    }

    public User getUser() {
        return user;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
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

    public List<DagRun> getDagRunList() {
        return dagRunList;
    }

    public void setDagRunList(List<DagRun> dagRunList) {
        this.dagRunList = dagRunList;
    }

    public UserTask getAddingUserTask() {
        return addingUserTask;
    }

    public void setAddingUserTask(UserTask addingUserTask) {
        this.addingUserTask = addingUserTask;
    }
}

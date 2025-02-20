package com.klimov.etl.vol_work.entity;

import java.util.ArrayList;
import java.util.List;

public class MainScreenState {
    private User user;
    private boolean isPause;
    private boolean signedIn;
    private List<DagRunUI> dagRunList;
    private UserTaskFromUI addingUserTask;

    public MainScreenState() {
        this.isPause = true;
        this.dagRunList = new ArrayList<>();
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

    public List<DagRunUI> getDagRunList() {
        return dagRunList;
    }

    public void setDagRunList(List<DagRunUI> dagRunList) {
        this.dagRunList = dagRunList;
    }

    public UserTaskFromUI getAddingUserTask() {
        return addingUserTask;
    }

    public void setAddingUserTask(UserTaskFromUI addingUserTask) {
        this.addingUserTask = addingUserTask;
    }
}

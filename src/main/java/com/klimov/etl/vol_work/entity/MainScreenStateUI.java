package com.klimov.etl.vol_work.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainScreenStateUI {

    private boolean isPause;
    private boolean signedIn;
    private boolean isInitDone;
    private List<DagRunUI> dagRunList;
    private List<DagRunUI> dagObserveList;

    public MainScreenStateUI() {
        this.isPause = true;
        this.signedIn = false;
        this.isInitDone = false;
        this.dagRunList = new ArrayList<>();
        this.dagObserveList = new ArrayList<>();
    }

    public List<DagRunUI> getDagObserveList() {
        return dagObserveList;
    }

    public void setDagObserveList(List<DagRunUI> dagObserveList) {
        this.dagObserveList = dagObserveList;
    }

    public boolean isInitDone() {
        return isInitDone;
    }

    public void setInitDone(boolean initDone) {
        isInitDone = initDone;
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

    public List<DagRunUI> getDagRunList() {
        return dagRunList;
    }

    public void setDagRunList(List<DagRunUI> dagRunList) {
        this.dagRunList = dagRunList;
    }
}

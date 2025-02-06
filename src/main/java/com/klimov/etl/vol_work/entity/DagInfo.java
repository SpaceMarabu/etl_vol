package com.klimov.etl.vol_work.entity;

public class DagInfo {

    private String dagId;
    private Boolean isPaused;

    public DagInfo() {
    }

    public DagInfo(Boolean isPaused) {
        this.isPaused = isPaused;
    }

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public Boolean getPaused() {
        return isPaused;
    }

    public void setPaused(Boolean paused) {
        isPaused = paused;
    }
}

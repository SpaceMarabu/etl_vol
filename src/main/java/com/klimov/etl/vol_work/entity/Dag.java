package com.klimov.etl.vol_work.entity;

public class Dag {

    private String dagId;
    private Boolean isPaused;

    public Dag() {
    }

    public Dag(Boolean isPaused) {
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

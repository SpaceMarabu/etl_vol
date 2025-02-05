package com.klimov.etl.vol_work.entity;


public class DagRun {
    private String conf;
    private String dagId;
    private String dagRunId;
    private String startDate;
    private String endDate;
    private String state;

    public DagRun() {
    }

    public DagRun(String conf, String dagId, String dagRunId, String startDate, String endDate, String state) {
        this.conf = conf;
        this.dagId = dagId;
        this.dagRunId = dagRunId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public String getDagRunId() {
        return dagRunId;
    }

    public void setDagRunId(String dagRunId) {
        this.dagRunId = dagRunId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

package com.klimov.etl.vol_work.entity;


public class DagRunUI {
    private String conf;
    private String dagId;
    private String dagRunId;
    private String startDate;
    private String endDate;
    private String state;
    private String comment;
    private String taskId;

    public DagRunUI() {
    }

    public DagRunUI(String conf, String dagId, String dagRunId, String startDate, String endDate, String state) {
        this.conf = conf;
        this.dagId = dagId;
        this.dagRunId = dagRunId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

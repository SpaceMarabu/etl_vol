package com.klimov.etl.vol_work.entity;

public class UserTaskFromUI {

    private String dagId;
    private RunType runType;
    private String taskId;
    private String listConfRAW;
    private String comment;

    public UserTaskFromUI(String userId, String dagId, RunType runType, String taskId, String listConf, String lastRunId) {
        this.dagId = dagId;
        this.runType = runType;
        this.taskId = taskId;
        this.listConfRAW = listConf;
        this.comment = "";
    }

    public UserTaskFromUI() {
        this.comment = "";
        this.runType = RunType.OBSERVE;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public RunType getRunType() {
        return runType;
    }

    public void setRunType(RunType runType) {
        this.runType = runType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getListConfRAW() {
        return listConfRAW;
    }

    public void setListConfRAW(String listConfRAW) {
        this.listConfRAW = listConfRAW;
    }
}

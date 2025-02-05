package com.klimov.etl.vol_work.entity;

import jakarta.persistence.*;

import java.util.List;

public class UserTask {

    private String userId;
    private String dagId;
    private RunType runType;
    private String taskId;
    private List<String> listConf;
    private String lastRunId;

    public UserTask(String userId, String dagId, RunType runType, String taskId, List<String> listConf, String lastRunId) {
        this.userId = userId;
        this.dagId = dagId;
        this.runType = runType;
        this.taskId = taskId;
        this.listConf = listConf;
        this.lastRunId = lastRunId;
    }

    public UserTask() {
    }

    public String getLastRunId() {
        return lastRunId;
    }

    public void setLastRunId(String lastRunId) {
        this.lastRunId = lastRunId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<String> getListConf() {
        return listConf;
    }

    public void setListConf(List<String> listConf) {
        this.listConf = listConf;
    }
}

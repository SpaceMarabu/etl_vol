package com.klimov.etl.vol_work.entity;

import java.util.List;

public class UserTask {

    private String userId;
    private String dagId;
    private RunType runType;
    private String taskId;
    private List<String> listConf;
    private String lastRunId;
    private int countErrors;
    private boolean isPause;
    private boolean isDone;

    public UserTask(String userId, String dagId, RunType runType, String taskId, List<String> listConf, String lastRunId) {
        this.userId = userId;
        this.dagId = dagId;
        this.runType = runType;
        this.taskId = taskId;
        this.listConf = listConf;
        this.lastRunId = lastRunId;
        this.countErrors = 0;
        this.isPause = false;
        this.isDone = false;
    }

    public UserTask() {
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isDone() {
        return isDone;
    }

    public void done() {
        isDone = true;
    }

    public int getCountErrors() {
        return countErrors;
    }

    public void incrementError() {
        if (++this.countErrors > 5) {
            isPause = true;
        }
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

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
    private String comment;

    public UserTask(String userId,
                    String dagId,
                    RunType runType,
                    String taskId,
                    List<String> listConf,
                    String comment,
                    int countErrors) {
        this.userId = userId;
        this.dagId = dagId;
        this.runType = runType;
        this.taskId = taskId;
        this.listConf = listConf;
        this.isPause = false;
        this.comment = comment;
        this.countErrors = countErrors;
    }

    public UserTask() {
        this.countErrors = 0;
        this.isPause = false;
        this.comment = "";
        this.runType = RunType.OBSERVE;
    }

    public void resetErrors() {
        this.countErrors = 0;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
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

    public void removeFirstConfig() {
        if (!this.listConf.isEmpty()) {
            this.listConf = this.listConf.subList(1, listConf.size());
        }
    }

    public void setListConf(List<String> listConf) {
        this.listConf = listConf;
    }
}

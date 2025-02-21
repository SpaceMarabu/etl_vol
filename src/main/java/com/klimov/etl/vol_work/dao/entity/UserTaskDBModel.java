package com.klimov.etl.vol_work.dao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_task")
public class UserTaskDBModel {

    @EmbeddedId
    UserTaskPK userTaskPK;

    @Column(name = "run_type")
    String runType;

    @Column(name = "task_id")
    String taskId;

    @Column(name = "list_conf")
    String listConf;

    @Column(name = "last_run_id")
    String lastRunId;

    @Column(name = "comment")
    String comment;

    @Column(name = "count_errors")
    int countErrors;

    public UserTaskDBModel(String userId,
                           String dagId,
                           String runType,
                           String taskId,
                           String listConf,
                           String lastRunId,
                           String comment,
                           int countErrors) {
        this.userTaskPK = new UserTaskPK(userId, dagId);
        this.runType = runType;
        this.taskId = taskId;
        this.listConf = listConf;
        this.lastRunId = lastRunId;
        this.comment = comment;
        this.countErrors = countErrors;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCountErrors() {
        return countErrors;
    }

    public void setCountErrors(int countErrors) {
        this.countErrors = countErrors;
    }

    public UserTaskDBModel() {
    }

    public UserTaskPK getUserTaskPK() {
        return userTaskPK;
    }

    public void setUserTaskPK(UserTaskPK userTaskPK) {
        this.userTaskPK = userTaskPK;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getListConf() {
        return listConf;
    }

    public void setListConf(String listConf) {
        this.listConf = listConf;
    }

    public String getLastRunId() {
        return lastRunId;
    }

    public void setLastRunId(String lastRunId) {
        this.lastRunId = lastRunId;
    }
}

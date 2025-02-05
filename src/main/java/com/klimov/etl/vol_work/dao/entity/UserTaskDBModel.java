package com.klimov.etl.vol_work.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_task")
public class UserTaskDBModel {

    @Id
    @Column(name = "user_id")
    String userId;

    @Column(name = "dag_id")
    String dagId;

    @Column(name = "run_type")
    String runType;

    @Column(name = "task_id")
    String taskId;

    @Column(name = "list_conf")
    String listConf;

    @Column(name = "last_run_id")
    String lastRunId;

    public UserTaskDBModel() {
    }

    public UserTaskDBModel(String userId, String dagId, String runType, String taskId, String listConf, String lastRunId) {
        this.userId = userId;
        this.dagId = dagId;
        this.runType = runType;
        this.taskId = taskId;
        this.listConf = listConf;
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

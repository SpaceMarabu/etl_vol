package com.klimov.etl.vol_work.entity;

import com.klimov.etl.vol_work.validation.CheckDagId;
import com.klimov.etl.vol_work.validation.CheckConfNotNullIfRunWithConf;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@CheckConfNotNullIfRunWithConf
public class UserTaskFromUI {

    @CheckDagId
    private String dagId;
    @NotNull
    private RunType runType;
    @NotBlank
    private String taskId;
    private String listConfRaw;
    private String comment;
    private int countErrors;

    public UserTaskFromUI(String userId, String dagId, RunType runType, String taskId, String listConf, int countErrors) {
        this.dagId = dagId;
        this.runType = runType;
        this.taskId = taskId;
        this.listConfRaw = listConf;
        this.comment = "";
        this.countErrors = countErrors;
    }

    public UserTaskFromUI() {
        this.dagId = "";
        this.runType = RunType.OBSERVE;
        this.taskId = "";
        this.listConfRaw = "";
        this.comment = "";
        this.countErrors = 0;
    }

    public void clear() {
        this.dagId = "";
        this.runType = RunType.OBSERVE;
        this.taskId = "";
        this.listConfRaw = "";
        this.comment = "";
        this.countErrors = 0;
    }

    public int getCountErrors() {
        return countErrors;
    }

    public void setCountErrors(int countErrors) {
        this.countErrors = countErrors;
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

    public String getListConfRaw() {
        return listConfRaw;
    }

    public void setListConfRaw(String listConfRaw) {
        this.listConfRaw = listConfRaw;
    }

    public class AddingTaskErrors {
        boolean isDagIdError;
        boolean isRunTypeError;
        boolean isTaskIdError;
        boolean isConfError;
    }

    public class AddingTaskErrorMessage {
        String dagIdError;
        String runTypeError;
        String taskIdError;
        String confError;
    }
}

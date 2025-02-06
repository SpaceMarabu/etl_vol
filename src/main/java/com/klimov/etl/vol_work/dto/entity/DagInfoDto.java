package com.klimov.etl.vol_work.dto.entity;

public class DagInfoDto {

    private String dagId;
    private Boolean isPaused;

    public DagInfoDto() {
    }

    public DagInfoDto(Boolean isPaused) {
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

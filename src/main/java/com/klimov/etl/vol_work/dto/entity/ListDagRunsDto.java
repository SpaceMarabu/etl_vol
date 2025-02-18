package com.klimov.etl.vol_work.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListDagRunsDto {

    @JsonProperty("dag_runs")
    List<DagRunInfoDto> dagRunInfoDtoList;
    @JsonProperty("total_entries")
    int totalEntries;

    public ListDagRunsDto() {
    }

    public ListDagRunsDto(List<DagRunInfoDto> dagRunInfoDtoList, int totalEntries) {
        this.dagRunInfoDtoList = dagRunInfoDtoList;
        this.totalEntries = totalEntries;
    }

    public List<DagRunInfoDto> getDagRunInfoDtoList() {
        return dagRunInfoDtoList;
    }

    public void setDagRunInfoDtoList(List<DagRunInfoDto> dagRunInfoDtoList) {
        this.dagRunInfoDtoList = dagRunInfoDtoList;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }
}

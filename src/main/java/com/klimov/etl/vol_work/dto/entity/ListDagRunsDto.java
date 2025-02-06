package com.klimov.etl.vol_work.dto.entity;

import java.util.List;

public class ListDagRunsDto {

    List<DagRunInfoDto> dagRunInfoDtoList;
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

package com.klimov.etl.vol_work.dto.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klimov.etl.vol_work.dto.mapper.RawConfDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DagRunInfoDto {

    @JsonDeserialize(using = RawConfDeserializer.class)
    @JsonProperty("conf")
    private String conf;
    @JsonProperty("dag_id")
    private String dagId;
    @JsonProperty("dag_run_id")
    private String dagRunId;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("state")
    private String state;

    public DagRunInfoDto() {
    }

    public DagRunInfoDto(String conf, String dagId, String dagRunId, String startDate, String endDate, String state) {
        this.conf = conf;
        this.dagId = dagId;
        this.dagRunId = dagRunId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }

    public String getConf() {
        return conf;
    }

    public String getDagId() {
        return dagId;
    }

    public String getDagRunId() {
        return dagRunId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getState() {
        return state;
    }
}

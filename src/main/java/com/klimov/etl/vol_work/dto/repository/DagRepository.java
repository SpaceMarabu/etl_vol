package com.klimov.etl.vol_work.dto.repository;

import com.klimov.etl.vol_work.dto.entity.DagInfoDto;
import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.dto.exceptions.DagNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.DagRunNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DagRepository {

    DagRunInfoDto getDagRunInfo(DagRunInfoDto dagRunInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagRunNotFoundException;

    DagRunInfoDto getLastDagRunInfo(DagInfoDto dagInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagRunNotFoundException;

    DagInfoDto getDagInfo(String dagId)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException;

    void pauseDag(DagInfoDto dagInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException;

    void unpauseDag(DagInfoDto dagInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException;

    DagRunInfoDto triggerDag(DagRunInfoDto dagRunInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException;

    DagRunInfoDto failDag(DagRunInfoDto dagRunInfoDto)
            throws IOException, InterruptedException, URISyntaxException;
}

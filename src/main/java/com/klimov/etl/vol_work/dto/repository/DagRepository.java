package com.klimov.etl.vol_work.dto.repository;

import com.klimov.etl.vol_work.dto.entity.DagInfoDto;
import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.dto.exceptions.DagNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.DagRunNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DagRepository {

    DagRunInfoDto getDagRunInfo(String dagId, String runId)
            throws IOException, InterruptedException, URISyntaxException, DagRunNotFoundException;

    DagRunInfoDto getLastDagRunInfo(String dagId)
            throws IOException, InterruptedException, URISyntaxException, DagRunNotFoundException;

    DagInfoDto getDagInfo(String dagId)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException;

    DagRunInfoDto triggerDag(String dagId, String conf)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException;

    DagRunInfoDto failDag(String dagId, String runId)
            throws IOException, InterruptedException, URISyntaxException, DagRunNotFoundException;

    void checkAccess(String login, String password)
            throws URISyntaxException, IOException, InterruptedException, UnauthorizedException;
}

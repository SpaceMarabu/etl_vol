package com.klimov.etl.vol_work.dto.entity;

import com.klimov.etl.vol_work.entity.Dag;
import com.klimov.etl.vol_work.entity.DagRun;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class DagRepositoryImpl implements DagRepository {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public DagRun getDagRunInfo(String dagId, String dagRunId) {
        return null;
    }

    @Override
    public Dag getDagInfo(String dagId) {
        return null;
    }

    @Override
    public void updateDagInfo(String dagId) {

    }
}

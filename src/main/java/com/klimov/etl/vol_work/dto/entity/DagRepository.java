package com.klimov.etl.vol_work.dto.entity;

import com.klimov.etl.vol_work.entity.Dag;
import com.klimov.etl.vol_work.entity.DagRun;

public interface DagRepository {

    DagRun getDagRunInfo(String dagId, String dagRunId);

    Dag getDagInfo(String dagId);

    void updateDagInfo(String dagId);
}

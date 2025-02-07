package com.klimov.etl.vol_work.dto.mapper;

import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.entity.DagRun;
import org.springframework.stereotype.Component;

@Component
public class ApiMapper {

    public DagRunInfoDto getDagRunInfoDtoFromEntity(DagRun entity) {
        return new DagRunInfoDto(
                entity.getConf(),
                entity.getDagId(),
                entity.getDagRunId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getState()
        );
    }

    public DagRun getDagRunInfoEntityFromDto(DagRunInfoDto dto) {
        return new DagRun(
                dto.getConf(),
                dto.getDagId(),
                dto.getDagRunId(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getState()
        );
    }
}

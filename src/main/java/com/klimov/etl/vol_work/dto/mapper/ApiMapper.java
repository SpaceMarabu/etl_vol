package com.klimov.etl.vol_work.dto.mapper;

import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.entity.DagRunUI;
import org.springframework.stereotype.Component;

@Component
public class ApiMapper {

    public DagRunInfoDto getDagRunInfoDtoFromEntity(DagRunUI entity) {
        return new DagRunInfoDto(
                entity.getConf(),
                entity.getDagId(),
                entity.getDagRunId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getState()
        );
    }

    public DagRunUI getDagRunInfoEntityFromDto(DagRunInfoDto dto) {
        return new DagRunUI(
                dto.getConf(),
                dto.getDagId(),
                dto.getDagRunId(),
                dto.getStartDate() == null ?
                        null : dto.getStartDate().substring(0, 19).replace("T", " "),
                dto.getEndDate(),
                dto.getState()
        );
    }
}

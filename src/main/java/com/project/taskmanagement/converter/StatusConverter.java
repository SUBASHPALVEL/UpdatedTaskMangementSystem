package com.project.taskmanagement.converter;

import org.springframework.stereotype.Component;

import com.project.taskmanagement.dto.StatusDTO;
import com.project.taskmanagement.entity.StatusEntity;

@Component
public class StatusConverter {
    public static StatusDTO convertToDTO(StatusEntity statusEntity) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatusId(statusEntity.getStatusId());
        statusDTO.setStatusLevel(statusEntity.getStatusLevel());
        return statusDTO;
    }

    public static StatusEntity convertToEntity(StatusDTO statusDTO){
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setStatusId(statusDTO.getStatusId());
        statusEntity.setStatusLevel(statusDTO.getStatusLevel());
        return statusEntity;
    }
}

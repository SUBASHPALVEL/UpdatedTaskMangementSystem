package com.project.taskmanagement.converter;

import org.springframework.stereotype.Component;

import com.project.taskmanagement.dto.PriorityDTO;
import com.project.taskmanagement.entity.PriorityEntity;

@Component
public class PriorityConverter {
    public static PriorityDTO convertToDTO(PriorityEntity priorityEntity) {
        PriorityDTO priorityDTO = new PriorityDTO();
        priorityDTO.setPriorityId(priorityEntity.getPriorityId());
        priorityDTO.setPriorityStatus(priorityEntity.getPriorityStatus());
        return priorityDTO;
    }

    public static PriorityEntity convertToEntity(PriorityDTO priorityDTO) {
        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setPriorityId(priorityDTO.getPriorityId());
        priorityEntity.setPriorityStatus(priorityDTO.getPriorityStatus());
        return priorityEntity;
    }
}

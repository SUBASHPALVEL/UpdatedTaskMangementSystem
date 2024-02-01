package com.project.taskmanagement.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.taskmanagement.dto.PriorityDTO;
import com.project.taskmanagement.dto.StatusDTO;
import com.project.taskmanagement.dto.TaskDTO;
import com.project.taskmanagement.entity.PriorityEntity;
import com.project.taskmanagement.entity.StatusEntity;
import com.project.taskmanagement.entity.TaskEntity;

@Component
public class TaskConverter {


    public static TaskDTO convertToDTO(TaskEntity taskEntity) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(taskEntity.getTaskId());
        taskDTO.setTitle(taskEntity.getTitle());
        taskDTO.setDescription(taskEntity.getDescription());

        StatusDTO statusDTO = StatusConverter.convertToDTO(taskEntity.getStatus());
        taskDTO.setStatus(statusDTO);

        PriorityDTO priorityDTO = PriorityConverter.convertToDTO(taskEntity.getPriority());
        taskDTO.setPriority(priorityDTO);

        taskDTO.setCreatedDate(taskEntity.getCreatedDate());
        taskDTO.setDueDate(taskEntity.getDueDate());
        taskDTO.setCompletedDate(taskEntity.getCompletedDate());
        taskDTO.setModifiedDate(taskEntity.getModifiedDate());
        return taskDTO;
    }

    public static TaskEntity convertToEntity(TaskDTO taskDTO) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDTO.getTitle());
        taskEntity.setDescription(taskDTO.getDescription());

        StatusEntity statusEntity = StatusConverter.convertToEntity(taskDTO.getStatus());
        taskEntity.setStatus(statusEntity);

        PriorityEntity priorityEntity = PriorityConverter.convertToEntity(taskDTO.getPriority());
        taskEntity.setPriority(priorityEntity);

        taskEntity.setDueDate(taskDTO.getDueDate());
        taskEntity.setCreatedDate(taskDTO.getCreatedDate());
        taskEntity.setCompletedDate(taskDTO.getCompletedDate());
        taskEntity.setModifiedDate(taskDTO.getModifiedDate());
        return taskEntity;
    }
}

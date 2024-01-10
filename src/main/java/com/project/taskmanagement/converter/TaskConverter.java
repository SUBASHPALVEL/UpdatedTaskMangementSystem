package com.project.taskmanagement.converter;

import org.springframework.stereotype.Component;

import com.project.taskmanagement.dto.TaskDTO;
import com.project.taskmanagement.entity.TaskEntity;
@Component
public class TaskConverter {
    
    public static TaskDTO convertToDTO(TaskEntity taskEntity){
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(taskEntity.getTaskId());
        taskDTO.setTitle(taskEntity.getTitle());
        taskDTO.setDescription(taskEntity.getDescription());
        taskDTO.setStatus(taskEntity.getStatus());
        taskDTO.setPriority(taskEntity.getPriority());
        taskDTO.setDueDate(taskEntity.getDueDate());
        return taskDTO;
    }

    public static TaskEntity convertToEntity(TaskDTO taskDTO) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDTO.getTitle());
        taskEntity.setDescription(taskDTO.getDescription());
        taskEntity.setStatus(taskDTO.getStatus());
        taskEntity.setPriority(taskDTO.getPriority());
        taskEntity.setDueDate(taskDTO.getDueDate());
        taskEntity.setCreatedDate(taskDTO.getCreatedDate());
        taskEntity.setCompletedDate(taskDTO.getCompletedDate());
        taskEntity.setModifiedDate(taskDTO.getModifiedDate());
        return taskEntity;
    }
}

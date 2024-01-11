package com.project.taskmanagement.service;

import java.util.List;

import com.project.taskmanagement.dto.TaskDTO;

public interface TaskService {
    String createTask(TaskDTO taskDTO);

    TaskDTO getTaskById(Long taskId);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getTasksByUserId(Long userId);

    String updateTask(Long taskId, TaskDTO taskDTO);

    String deleteTask(Long taskId);
}

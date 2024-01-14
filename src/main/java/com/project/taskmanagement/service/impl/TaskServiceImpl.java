package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.Repository.TaskRepository;
import com.project.taskmanagement.converter.TaskConverter;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.TaskDTO;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.TaskEntity;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService{
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskConverter taskConverter;

    @Override
    public String createTask(TaskDTO taskDTO) {
        TaskEntity taskEntity= taskConverter.convertToEntity(taskDTO);
        List<UserDTO> usersDTOs = taskDTO.getAssignedUsers();
        List<UserEntity> usersEntities = new ArrayList<>();
        for (UserDTO userdto : usersDTOs) {
            usersEntities.add(UserConverter.convertToEntity(userdto));
        }
        taskEntity.setAssignedUsers(usersEntities);
        taskEntity.setActive(true);
        taskRepository.save(taskEntity);
        return "Task created successfully";
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            TaskEntity taskEntity = taskOptional.get();
            TaskDTO taskDTO = taskConverter.convertToDTO(taskEntity);
            List<UserEntity> usersEntities = taskEntity.getAssignedUsers();
            List<UserDTO> usersDTOs = new ArrayList<>();
            for (UserEntity userEntity : usersEntities) {
                usersDTOs.add(UserConverter.convertToDTO(userEntity));
            } 
            taskDTO.setAssignedUsers(usersDTOs);
            return taskDTO;
        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("TASK_NOT_FOUND");
            errorModel.setMessage("Task not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<TaskEntity> tasks = taskRepository.findByIsActiveTrue();
        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (TaskEntity task : tasks) {
            List<UserEntity> usersEntities = task.getAssignedUsers();
            List<UserDTO> usersDTOList = new ArrayList<>();
            for (UserEntity user : usersEntities) {
                usersDTOList.add(UserConverter.convertToDTO(user));
            }

            TaskDTO taskDTO = taskConverter.convertToDTO(task);
            taskDTO.setAssignedUsers(usersDTOList);
            taskDTOList.add(taskDTO);
        }
        return taskDTOList;
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Long userId) {
        List<TaskEntity> tasks = taskRepository.findByAssignedUsers_UserIdAndIsActiveTrue(userId); 
        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (TaskEntity task : tasks) {
            List<UserEntity> usersEntities = task.getAssignedUsers();
            List<UserDTO> usersDTOList = new ArrayList<>();
            for (UserEntity user : usersEntities) {
                usersDTOList.add(UserConverter.convertToDTO(user));
            }

            TaskDTO taskDTO = taskConverter.convertToDTO(task);
            taskDTO.setAssignedUsers(usersDTOList);
            taskDTOList.add(taskDTO);
        }
        return taskDTOList;
        // List<TaskDTO> taskDTOList = new ArrayList<>();
        // for (TaskEntity task : tasks) {
        //     TaskDTO taskDTO = taskConverter.convertToDTO(task);
        //     taskDTOList.add(taskDTO);
        // }
        // return taskDTOList;
    }

    @Override
    public String updateTask(Long taskId, TaskDTO taskDTO) {
        TaskEntity existingTaskEntity = taskRepository.findById(taskId).orElse(null);
        if (existingTaskEntity != null) {
            TaskEntity updatedTaskEntity = taskConverter.convertToEntity(taskDTO);
            List<UserDTO> updatedAssignedUsers =taskDTO.getAssignedUsers();
            List<UserEntity> updatedAssignedUsersEntities = new ArrayList<UserEntity>();
            for (UserDTO user : updatedAssignedUsers) {
                UserEntity updatedUserEntity = UserConverter.convertToEntity(user);
                updatedAssignedUsersEntities.add(updatedUserEntity);
            }
            updatedTaskEntity.setAssignedUsers(updatedAssignedUsersEntities);
            updatedTaskEntity.setTaskId(taskId);
            taskRepository.save(updatedTaskEntity);
            return "Task updated successfully";
        }
        else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("TASK_NOT_FOUND");
            errorModel.setMessage("Task not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    }

    @Override
    public String deleteTask(Long taskId) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            TaskEntity task = taskOptional.get();
            task.setActive(false);
            taskRepository.save(task);
            return "Task deleted successfully";
        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("TASK_NOT_FOUND");
            errorModel.setMessage("Task not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    }
}

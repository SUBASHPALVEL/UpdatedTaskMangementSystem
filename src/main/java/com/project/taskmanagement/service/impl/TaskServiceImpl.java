package com.project.taskmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.converter.TaskConverter;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.TaskDTO;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.AuditEntity;
import com.project.taskmanagement.entity.TaskEntity;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.repository.AuditRepository;
import com.project.taskmanagement.repository.TableRegistryRepository;
import com.project.taskmanagement.repository.TaskRepository;
import com.project.taskmanagement.service.CurrentUserService;
import com.project.taskmanagement.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskConverter taskConverter;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TableRegistryRepository tableRegistryRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    public String createTask(TaskDTO taskDTO) {
        TaskEntity taskEntity = taskConverter.convertToEntity(taskDTO);
        List<UserDTO> usersDTOs = taskDTO.getAssignedUsers();
        List<UserEntity> usersEntities = new ArrayList<>();
        for (UserDTO userdto : usersDTOs) {

            UserEntity userEntity = UserConverter.convertToEntity(userdto);

            if (!userEntity.isActive()) {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(messageSource.getMessage("user.not.active.code",
                        new Object[] { userEntity.getUserId() }, LocaleContextHolder.getLocale()));
                errorModel.setMessage(
                        messageSource.getMessage("user.not.active.message", new Object[] { userEntity.getUserId() },
                                LocaleContextHolder.getLocale()));
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);

            }

            usersEntities.add(userEntity);
        }
        taskEntity.setAssignedUsers(usersEntities);
        taskEntity.setActive(true);

        LocalDateTime now = LocalDateTime.now();
        taskEntity.setCreatedAt(now);
        taskEntity.setCreatedBy(currentUserService.getCurrentUserId());
        taskRepository.save(taskEntity);

        String modifiedValue = taskEntity.toString();
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setModifiedValue(modifiedValue);
        Long tableId = tableRegistryRepository.getTableIdByTableName("tasks").getTableId();
        auditEntity.setTableId(tableId);
        auditEntity.setAction("create");
        auditRepository.save(auditEntity);

        return messageSource.getMessage("task.created", null, LocaleContextHolder.getLocale());
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
            errorModel.setCode(messageSource.getMessage("task.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("task.not_found.message", null, LocaleContextHolder.getLocale()));
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
    }

    @Override
    public String updateTask(Long taskId, TaskDTO taskDTO) {
        TaskEntity existingTaskEntity = taskRepository.findById(taskId).orElse(null);
        if (existingTaskEntity != null) {
            TaskEntity updatedTaskEntity = taskConverter.convertToEntity(taskDTO);
            List<UserDTO> updatedAssignedUsers = taskDTO.getAssignedUsers();
            List<UserEntity> updatedAssignedUsersEntities = new ArrayList<UserEntity>();
            for (UserDTO user : updatedAssignedUsers) {
                UserEntity updatedUserEntity = UserConverter.convertToEntity(user);
                updatedAssignedUsersEntities.add(updatedUserEntity);
            }
            updatedTaskEntity.setAssignedUsers(updatedAssignedUsersEntities);
            updatedTaskEntity.setActive(true);
            updatedTaskEntity.setTaskId(taskId);

            LocalDateTime now = LocalDateTime.now();
            updatedTaskEntity.setLastModifiedAt(now);
            updatedTaskEntity.setLastModifiedBy(currentUserService.getCurrentUserId());

            taskRepository.save(updatedTaskEntity);

            String modifiedValue = updatedTaskEntity.toString();
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setModifiedValue(modifiedValue);
            Long tableId = tableRegistryRepository.getTableIdByTableName("tasks").getTableId();
            auditEntity.setTableId(tableId);
            auditEntity.setAction("update");
            auditEntity.setLastModifiedAt(now);
            auditEntity.setLastModifiedBy(currentUserService.getCurrentUserId());
            auditRepository.save(auditEntity);

            return messageSource.getMessage("task.updated", null, LocaleContextHolder.getLocale());
        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(messageSource.getMessage("task.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("task.not_found.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    }

    @Override
    public String deleteTask(Long taskId) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            TaskEntity taskEntity = taskOptional.get();
            taskEntity.setActive(false);
            LocalDateTime now = LocalDateTime.now();
            taskEntity.setLastModifiedAt(now);
            taskEntity.setLastModifiedBy(currentUserService.getCurrentUserId());
            taskRepository.save(taskEntity);

            String modifiedValue = taskEntity.toString();
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setModifiedValue(modifiedValue);
            Long tableId = tableRegistryRepository.getTableIdByTableName("tasks").getTableId();
            auditEntity.setTableId(tableId);
            auditEntity.setAction("delete");
            auditEntity.setLastModifiedAt(now);
            auditEntity.setLastModifiedBy(currentUserService.getCurrentUserId());
            auditRepository.save(auditEntity);

            return messageSource.getMessage("task.deleted", null, LocaleContextHolder.getLocale());
        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(messageSource.getMessage("task.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("task.not_found.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    }
}
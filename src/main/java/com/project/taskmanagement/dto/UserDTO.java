package com.project.taskmanagement.dto;

import java.util.ArrayList;
import java.util.List;

import com.project.taskmanagement.entity.RoleEntity;
import com.project.taskmanagement.entity.TaskEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private Long userId;

    @NotNull(message = "User Name is mandatory")
    @NotEmpty(message = "User Name cannot be empty")
    private String userName;

    @NotNull(message = "user Mail is mandatory")
    @NotEmpty(message = "User Mail cannot be empty")
    private String userMail;

    @NotNull(message = "Password is mandatory")
    private String password;

    private RoleEntity roleId;

    @NotNull(message = "Is Active is mandatory")
    private boolean isActive;

    private List<TaskEntity> assignedTasks = new ArrayList<>();
}

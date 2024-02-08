package com.project.taskmanagement.dto;

import java.util.ArrayList;
import java.util.List;

import com.project.taskmanagement.entity.RoleEntity;
import com.project.taskmanagement.entity.TaskEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing user information.
 * @author Subash Palvel
 * @since 07/02/2024
 */

@Setter
@Getter
public class UserDTO {
    private Long userId;

    @NotNull(message = "Name is mandatory")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "User Name is mandatory")
    @NotEmpty(message = "User Name cannot be empty")
    private String userName;

    @NotNull(message = "user Mail is mandatory")
    @NotEmpty(message = "User Mail cannot be empty")
    private String userMail;

    @NotNull(message = "Password is mandatory")
    private String password;

    @NotNull(message = "Role is mandatory")
    private RoleEntity roleId;

    @NotNull(message = "Is Active is mandatory")
    private boolean isActive;

    private List<TaskEntity> assignedTasks = new ArrayList<>();

    private String token;

    private String oldPassword;

    private String newPassword;

}

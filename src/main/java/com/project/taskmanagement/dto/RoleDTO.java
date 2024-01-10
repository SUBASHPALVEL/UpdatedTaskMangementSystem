package com.project.taskmanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
    
    private Long roleId;

    @NotNull(message = "Designation is mandatory")
    @NotEmpty(message = "Designation cannot be empty")
    private String designation;
}

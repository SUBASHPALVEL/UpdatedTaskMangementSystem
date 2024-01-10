package com.project.taskmanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDTO {
    private long statusId;

    @NotNull(message = "Status Level is mandatory")
    @NotEmpty(message = "Status Level cannot be empty")
    private String statusLevel;
}

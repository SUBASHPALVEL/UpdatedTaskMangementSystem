package com.project.taskmanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PriorityDTO {
    private Long priorityId;

    @NotNull(message = "Priority Status is mandatory")
    @NotEmpty(message = "Priority Status cannot be empty")
    private String priorityStatus;
}

package com.project.taskmanagement.dto;

import java.time.LocalDate;
import java.util.List;

import com.project.taskmanagement.entity.PriorityEntity;
import com.project.taskmanagement.entity.StatusEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    
    private Long taskId;

    @NotNull(message = "Task Title is mandatory")
    @NotEmpty(message = "Task Title cannot be empty")
    private String title;

    private String description;

    @NotNull(message = "Task Status is mandatory")
    @NotEmpty(message = "Task Status cannot be empty")
    private StatusEntity status;

    @NotNull(message = "Task Priority is mandatory")
    @NotEmpty(message = "Task Priority cannot be empty")
    private PriorityEntity priority;

    @NotNull(message = "Task Due Date is mandatory")
    @NotEmpty(message = "Task Due Date cannot be empty")
    private LocalDate dueDate;

    private LocalDate createdDate;

    private LocalDate completedDate;

    private LocalDate modifiedDate;

    private List<UserDTO> assignedUsers;

}




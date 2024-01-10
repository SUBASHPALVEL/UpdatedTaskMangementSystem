package com.project.taskmanagement.entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tasks")

public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "title", nullable = false)
    @NotNull
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false)

    private Status status;

    @Column(name = "priority", nullable = false)

    private Priority priority;

    @Column(name = "due_Date")
    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    @Column(name = "created_Date")
    @Temporal(TemporalType.DATE)
    private LocalDate createdDate;

    @Column(name = "completed_Date")
    @Temporal(TemporalType.DATE)
    private LocalDate completedDate;

    @Column(name = "modified_Date")
    @Temporal(TemporalType.DATE)
    private LocalDate modifiedDate;

    @ManyToMany
    @JoinTable(name="tasks_users",
                    joinColumns = @JoinColumn(name="taskId"),
                    inverseJoinColumns = @JoinColumn(name="userId")
    )
    private List<UserEntity> assignedUsers = new ArrayList<>();
    
}

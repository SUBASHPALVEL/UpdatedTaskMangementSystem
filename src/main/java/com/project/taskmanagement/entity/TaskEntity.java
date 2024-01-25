package com.project.taskmanagement.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tasks")

public class TaskEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Size(min = 2, max = 100, message = "Title must be between 02 and 150 of size")
    @Column(name = "title", nullable = false)
    @NotNull
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusEntity status;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private PriorityEntity priority;

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

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @JoinTable(name = "tasks_users", joinColumns = @JoinColumn(name = "taskId"), inverseJoinColumns = @JoinColumn(name = "userId"))
    @ManyToMany
    private List<UserEntity> assignedUsers = new ArrayList<>();

}

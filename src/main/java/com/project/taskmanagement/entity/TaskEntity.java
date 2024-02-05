package com.project.taskmanagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.stream.Collectors;

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


public class TaskEntity {
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

    @NotAudited
    @CreatedBy
    @Column(name = "created_by", updatable = false )
    private Long createdBy;

    @NotAudited
    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by" , insertable = false)
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at" , insertable = false)
    private LocalDateTime lastModifiedAt;

    @Column(name = "due_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dueAt;

    @Column(name = "completed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @JoinTable(name = "users_tasks", joinColumns = @JoinColumn(name = "taskId"), inverseJoinColumns = @JoinColumn(name = "userId"))
    @ManyToMany
    private List<UserEntity> assignedUsers = new ArrayList<>();


    @Override
    public String toString() {
    return "TaskEntity{" +
            "taskId=" + taskId +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", status=" + status.getStatusId()  + 
            ", priority=" + priority.getPriorityId() + 
            ", dueAt=" + dueAt +
            ", completedAt=" + completedAt +
            ", isActive=" + isActive +
            ", assignedUsers=" + assignedUsers.stream().map(user -> user.getUserId().toString()).collect(Collectors.joining(", ")) +
            '}';
}

}

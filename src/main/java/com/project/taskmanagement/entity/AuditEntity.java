package com.project.taskmanagement.entity;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@Table(name = "audit_log")
public class AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private long logId;

    @Column(name = "table_id",nullable = false)
    private Long tableId;

    @Column(name = "modified_value", columnDefinition = "TEXT")
    private String modifiedValue;

    @Column(name = "action")
    private String action;

    @LastModifiedBy
    @Column(name = "last_modified_by" , insertable = false)
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at" , insertable = false)
    private LocalDateTime lastModifiedAt;

}

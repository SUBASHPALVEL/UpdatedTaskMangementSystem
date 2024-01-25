package com.project.taskmanagement.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.ToString;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@ToString
public class BaseEntity {

    @CreatedBy
    @Column(name = "created_by", updatable = false )
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by" , insertable = false)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at" , insertable = false)
    private LocalDateTime lastModifiedAt;





}

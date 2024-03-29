package com.project.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.AuditEntity;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, Long> {

}

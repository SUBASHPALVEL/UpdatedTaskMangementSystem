package com.project.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.StatusEntity;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Long> {

    boolean existsByStatusLevel(String statusLevel);

    StatusEntity getStatusIdByStatusLevel(String statusLevel);

}

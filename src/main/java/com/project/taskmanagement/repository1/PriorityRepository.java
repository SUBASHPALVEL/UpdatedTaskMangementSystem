package com.project.taskmanagement.repository1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.PriorityEntity;

@Repository
public interface PriorityRepository extends JpaRepository<PriorityEntity, Long> {
    boolean existsByPriorityStatus(String priorityStatus);
}

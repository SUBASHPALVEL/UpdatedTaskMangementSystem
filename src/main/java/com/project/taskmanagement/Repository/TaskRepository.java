package com.project.taskmanagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByIsActiveTrue();

    List<TaskEntity> findByAssignedUsers_UserIdAndIsActiveTrue(Long userId);

}

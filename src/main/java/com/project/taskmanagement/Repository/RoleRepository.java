package com.project.taskmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long>{
    boolean existsByDesignation(String designation);
}

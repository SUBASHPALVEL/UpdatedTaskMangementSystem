package com.project.taskmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Boolean existsByDesignation(String designation);

    Optional<RoleEntity> findByDesignation(String designation);

    RoleEntity getRoleIdByDesignation(String designation);

}

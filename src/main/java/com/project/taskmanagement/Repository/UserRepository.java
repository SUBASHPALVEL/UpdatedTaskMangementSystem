package com.project.taskmanagement.Repository;

import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    
    Optional<UserEntity> findByUserMail(String userMail);

    List<UserEntity> findByIsActiveTrue();
}
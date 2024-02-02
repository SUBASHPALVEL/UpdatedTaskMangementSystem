package com.project.taskmanagement.Repository;

import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> ,RevisionRepository<UserEntity, Long, Long>{

    Optional<UserEntity> findByUserMail(String userMail);

    List<UserEntity> findByIsActiveTrue();

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByUserNameAndUserMail(String userName, String userMail);

}

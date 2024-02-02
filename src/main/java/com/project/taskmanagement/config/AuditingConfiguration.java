package com.project.taskmanagement.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.entity.UserEntity;

@Configuration
public class AuditingConfiguration implements AuditorAware<Long>  {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<Long> getCurrentAuditor() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    return userRepository.findByUserName(username)
            .map(UserEntity::getUserId);
}
    
}

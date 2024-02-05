package com.project.taskmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.repository.UserRepository;
import com.project.taskmanagement.service.CurrentUserService;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Long currentUserId = userRepository.findByUserName(username).get().getUserId();
            return currentUserId;
        }

        return 1L;
    }
}

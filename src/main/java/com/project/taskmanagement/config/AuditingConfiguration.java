package com.project.taskmanagement.config;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    Collection userAuthoritCollection =  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    Optional<SimpleGrantedAuthority> userRole = userAuthoritCollection.stream().findFirst();
    String userAuthority=userRole.get().getAuthority();
    System.out.println(userAuthority);

    if (username.equals("anonymousUser") && userAuthority.equals("ROLE_ANONYMOUS")) {
        return Optional.of(0L);
    }
    return userRepository.findByUserName(username)
            .map(UserEntity::getUserId);
            
}
    
}

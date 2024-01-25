package com.project.taskmanagement.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

    public String generateJwt(Authentication auth);

}

package com.project.taskmanagement.service;

import com.project.taskmanagement.dto.UserDTO;

public interface AuthenticationService {

    String createAdminUser(UserDTO userDTO);

    UserDTO loginAdminUser (String userName, String password);
}

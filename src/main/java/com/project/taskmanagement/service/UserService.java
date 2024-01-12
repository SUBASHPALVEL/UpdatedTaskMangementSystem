package com.project.taskmanagement.service;

import java.util.List;

import com.project.taskmanagement.dto.UserDTO;

public interface UserService {
    String createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    String updateUser(Long userId, UserDTO userDTO);

    String deleteUser(Long userId);

    String createAdminUser(UserDTO userDTO);

    String loginAdminUser(String userMail, String password);
}

package com.project.taskmanagement.service;

import java.util.List;

import com.project.taskmanagement.dto.UserDTO;
/**
 * Interface for providing functionality for user management.
 * @author Subash Palvel
 * @since 07/02/2024
 */
public interface UserService {
    String createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    String updateUser(Long userId, UserDTO userDTO);

    String deleteUser(Long userId);

    String changePassword(UserDTO userDTO);
}

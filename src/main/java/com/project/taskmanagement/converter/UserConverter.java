package com.project.taskmanagement.converter;

import org.springframework.stereotype.Component;

import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.UserEntity;

/**
 * Converter class for converting UserEntity objects to UserDTO objects and vice
 * versa.
 * 
 * @author Subash Palvel
 * @since 07/02/2024
 */

@Component
public class UserConverter {

    /**
     * Converts a UserEntity object to a UserDTO object.
     * 
     * @param userEntity The UserEntity object to convert.
     * @return The corresponding UserDTO object.
     */

    public static UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setName(userEntity.getName());
        userDTO.setUserName(userEntity.getUsername());
        userDTO.setUserMail(userEntity.getUserMail());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setRoleId(userEntity.getRoleId());
        userDTO.setActive(userEntity.isActive());
        return userDTO;
    }

    /**
     * Converts a UserDTO object to a UserEntity object.
     * 
     * @param userDTO The UserDTO object to convert.
     * @return The corresponding UserEntity object.
     */

    public static UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setUserMail(userDTO.getUserMail());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setRoleId(userDTO.getRoleId());
        return userEntity;
    }
}
package com.project.taskmanagement.converter;

import org.springframework.stereotype.Component;

import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.UserEntity;

@Component
public class UserConverter {
    
    public static UserDTO convertToDTO(UserEntity userEntity) {
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(userEntity.getUserId());
    userDTO.setRoleId(userEntity.getRoleId());
    return userDTO;
}

    public static UserEntity convertToEntity(UserDTO userDTO){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setUserMail(userDTO.getUserMail());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setRoleId(userDTO.getRoleId());
        userEntity.setActive(userDTO.isActive());
        return userEntity;

    }

}
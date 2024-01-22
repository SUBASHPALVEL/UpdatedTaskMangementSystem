package com.project.taskmanagement.converter;

import org.springframework.stereotype.Component;

import com.project.taskmanagement.dto.RoleDTO;
import com.project.taskmanagement.entity.RoleEntity;

@Component
public class RoleConverter {
    public static RoleDTO convertToDTO(RoleEntity roleEntity) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(roleEntity.getRoleId());
        roleDTO.setDesignation(roleEntity.getDesignation());
        return roleDTO;
    }

    public static RoleEntity convertToEntity(RoleDTO roleDTO) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(roleDTO.getRoleId());
        roleEntity.setDesignation(roleDTO.getDesignation());
        return roleEntity;
    }
}

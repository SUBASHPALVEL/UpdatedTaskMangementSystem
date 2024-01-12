package com.project.taskmanagement.service;

import java.util.List;

import com.project.taskmanagement.dto.RoleDTO;

public interface RoleService {

    void createRole(RoleDTO roleDTO);

    RoleDTO getRoleByUserId(Long userId);

    List<RoleDTO> getAllRoles();

    void updateRole(Long roleId, RoleDTO roleDTO);

    void deleteRole(Long roleId);
}

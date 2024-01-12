package com.project.taskmanagement.service;

import java.util.List;

import com.project.taskmanagement.dto.RoleDTO;

public interface RoleService {

    String createRole(RoleDTO roleDTO);

    RoleDTO getRoleByUserId(Long userId);

    List<RoleDTO> getAllRoles();

    String updateRole(Long roleId, RoleDTO roleDTO);

    String deleteRole(Long roleId);
}

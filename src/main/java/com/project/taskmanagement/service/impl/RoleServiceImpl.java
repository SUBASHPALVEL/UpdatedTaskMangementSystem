package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.Repository.RoleRepository;
import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.converter.RoleConverter;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.RoleDTO;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.RoleEntity;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String createRole (RoleDTO roleDTO) {
        if (roleRepository.existsByDesignation(roleDTO.getDesignation())) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("DESIGNATION_ALREADY_EXIST");
            errorModel.setMessage("Role with designation already exists");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setDesignation(roleDTO.getDesignation());
        roleRepository.save(roleEntity);
        return "Role created successfully";
    }

    @Override
    public RoleDTO getRoleByUserId(Long userId) {

        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);

        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            RoleEntity roleEntity = userEntity.getRoleId();
            RoleDTO roleDTO = RoleConverter.convertToDTO(roleEntity);
            return roleDTO;
        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("USER_NOT_FOUND");
            errorModel.setMessage("User not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    
    }

    @Override
    public List<RoleDTO> getAllRoles() {

        List<RoleEntity> roles = roleRepository.findAll();
        List<RoleDTO> rolesDTOs = new ArrayList<>();
        for (RoleEntity role : roles) {
            RoleDTO roleDTO = RoleConverter.convertToDTO(role);
            rolesDTOs.add(roleDTO);
        }
        return rolesDTOs;

    }

    @Override
    public String updateRole(Long roleId, RoleDTO roleDTO) {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(roleId);
        if (optionalRoleEntity.isEmpty()) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("ROLE_NOT_FOUND");
            errorModel.setMessage("Role not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        RoleEntity roleEntity = optionalRoleEntity.get();
        roleEntity.setDesignation(roleDTO.getDesignation());
        roleRepository.save(roleEntity);
        return "Role is updated successfully";
    }

    @Override
    public String deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("ROLE_NOT_FOUND");
            errorModel.setMessage("Role not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        roleRepository.deleteById(roleId);
        return "Role deleted";
    }
}
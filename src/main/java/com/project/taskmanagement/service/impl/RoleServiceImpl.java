package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.converter.RoleConverter;
import com.project.taskmanagement.dto.RoleDTO;
import com.project.taskmanagement.entity.RoleEntity;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.repository.RoleRepository;
import com.project.taskmanagement.repository.UserRepository;
import com.project.taskmanagement.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String createRole(RoleDTO roleDTO) {
        if (roleRepository.existsByDesignation(roleDTO.getDesignation())) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("role.exists.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("role.exists.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setDesignation(roleDTO.getDesignation());
        roleRepository.save(roleEntity);
        return messageSource.getMessage("role.created", null, LocaleContextHolder.getLocale());
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
            errorModel.setCode(messageSource.getMessage("user.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("user.not_found.message", null, LocaleContextHolder.getLocale()));
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
            errorModel.setCode(messageSource.getMessage("role.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("role.not_found.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        RoleEntity roleEntity = optionalRoleEntity.get();
        roleEntity.setDesignation(roleDTO.getDesignation());
        roleRepository.save(roleEntity);
        return messageSource.getMessage("role.updated", null, LocaleContextHolder.getLocale());
    }

    @Override
    public String deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(messageSource.getMessage("role.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("role.not_found.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        roleRepository.deleteById(roleId);
        return messageSource.getMessage("role.deleted", null, LocaleContextHolder.getLocale());
    }
}
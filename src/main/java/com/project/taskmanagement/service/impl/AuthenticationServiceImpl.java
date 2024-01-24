package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.converter.RoleConverter;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.RoleDTO;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.RoleEntity;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.service.AuthenticationService;
import com.project.taskmanagement.service.TokenService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String createAdminUser(UserDTO userDTO) {
        Optional<UserEntity> existingUser = userRepository.findByUserName(userDTO.getUserName());

        if (existingUser.isPresent()) {
            if (existingUser.get().isActive()) {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(messageSource.getMessage("user.exists.code", null, LocaleContextHolder.getLocale()));
                errorModel.setMessage(messageSource.getMessage("user.exists.message", null, LocaleContextHolder.getLocale()));
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);

            } else {
                existingUser.get().setActive(true);

                String password = userDTO.getPassword();
                String encodedPassword = passwordEncoder.encode(password);
                existingUser.get().setPassword(encodedPassword);

                RoleDTO adminDTO = new RoleDTO();
                adminDTO.setRoleId((long) 1);
                existingUser.get().setRoleId(RoleConverter.convertToEntity(adminDTO));
                userRepository.save(existingUser.get());
                return messageSource.getMessage("user.activated", null, LocaleContextHolder.getLocale());
            }
        } else {

            UserEntity newUser = UserConverter.convertToEntity(userDTO);

            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);

            newUser.setActive(true);

            RoleEntity adminEntity = new RoleEntity();
            adminEntity.setRoleId((long) 1);
            newUser.setRoleId(adminEntity);

            newUser.setPassword(encodedPassword);

            userRepository.save(newUser);
            return messageSource.getMessage("user.created", null, LocaleContextHolder.getLocale());
        }
    }

    @Override
    public UserDTO loginAdminUser(String userName, String password) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, password));

            String token = tokenService.generateJwt(auth);

            Optional<UserEntity> userOptional = userRepository.findByUserName(userName);
            if (userOptional.isPresent()) {
                UserEntity userEntity = userOptional.get();
                UserDTO userDTO = userConverter.convertToDTO(userEntity);
                userDTO.setToken(token);
                return userDTO;
            } else {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(messageSource.getMessage("user.not_found.code", null, LocaleContextHolder.getLocale()));
                errorModel.setMessage(messageSource.getMessage("user.not_found.message", null, LocaleContextHolder.getLocale()));
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);
            }

        } catch (AuthenticationException e) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(messageSource.getMessage("user.error.authentication.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(messageSource.getMessage("user.error.authentication.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    }

}

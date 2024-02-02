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

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String createAdminUser(UserDTO userDTO) {

        Optional<UserEntity> existingUserByMail = userRepository.findByUserMail(userDTO.getUserMail());

        if (existingUserByMail.isPresent()) {
            System.out.println("Mail already exists: ");

            Optional<UserEntity> existingUserByName = userRepository.findByUserName(userDTO.getUserName());
            if (existingUserByName.isPresent()) {
                System.out.println("Mail already exists + UserName exists ");

                if (existingUserByName.get().equals(existingUserByMail.get())) {

                    System.out.println("Mail already exists + UserName exists + Same Account");

                    if (existingUserByMail.get().isActive()) {

                        System.out.println("Mail already exists + UserName exists + Same Account + Active");
                        List<ErrorModel> errorModelList = new ArrayList<>();
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setCode(
                                messageSource.getMessage("user.exists.code", null, LocaleContextHolder.getLocale()));
                        errorModel.setMessage(
                                messageSource.getMessage("user.exists.message", null, LocaleContextHolder.getLocale()));
                        errorModelList.add(errorModel);
                        throw new BusinessException(errorModelList);

                    } else {

                        System.out.println("Mail already exists + UserName exists + Same Account + Not Active");

                        existingUserByMail.get().setActive(true);

                        String password = userDTO.getPassword();
                        String encodedPassword = passwordEncoder.encode(password);
                        existingUserByMail.get().setPassword(encodedPassword);

                        boolean adminRole = roleRepository.existsByDesignation("ADMIN");
                        if (adminRole) {

                            RoleEntity adminRoleId = roleRepository.getRoleIdByDesignation("ADMIN");

                            RoleDTO adminDTO = new RoleDTO();
                            adminDTO.setRoleId(adminRoleId.getRoleId());

                            existingUserByMail.get().setRoleId(RoleConverter.convertToEntity(adminDTO));

                            userRepository.save(existingUserByMail.get());
                            return messageSource.getMessage("user.activated", null, LocaleContextHolder.getLocale());
                        } else {
                            return messageSource.getMessage("role.not_found.code", null,
                                    LocaleContextHolder.getLocale());
                        }

                    }

                } else {
                    System.out.println("Mail already exists + UserName exists + Different Account");
                    return messageSource.getMessage("user.mail.exists.name.exists.not_match", null,
                            LocaleContextHolder.getLocale());
                }

            } else {
                System.out.println("Mail already exists + UserName Not exists ");
                return messageSource.getMessage("user.mail.exists.name.not_match", null,
                        LocaleContextHolder.getLocale());
            }

        } else {
            System.out.println("Mail Not exists: ");

            Optional<UserEntity> existingUserByName = userRepository.findByUserName(userDTO.getUserName());
            if (existingUserByName.isPresent()) {

                System.out.println("Mail Not exists + UserName exists ");
                return messageSource.getMessage("user.name.exists.mail.not_match", null,
                        LocaleContextHolder.getLocale());

            } else {
                System.out.println("Mail Not exists + UserName Not exists ");

                boolean adminRole = roleRepository.existsByDesignation("ADMIN");
                if (adminRole) {
                    UserEntity newUser = UserConverter.convertToEntity(userDTO);

                    String password = userDTO.getPassword();
                    String encodedPassword = passwordEncoder.encode(password);

                    newUser.setActive(true);

                    newUser.setPassword(encodedPassword);

                    RoleEntity adminRoleId = roleRepository.getRoleIdByDesignation("ADMIN");

                    RoleDTO adminDTO = new RoleDTO();
                    adminDTO.setRoleId(adminRoleId.getRoleId());

                    newUser.setRoleId(RoleConverter.convertToEntity(adminDTO));

                    userRepository.save(newUser);
                    return messageSource.getMessage("user.created", null, LocaleContextHolder.getLocale());
                } else {
                    return messageSource.getMessage("role.not_found.code", null, LocaleContextHolder.getLocale());

                }

            }

        }

    }

    @Override
    public UserDTO loginAdminUser(String userName, String password) {

        Optional<UserEntity> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isPresent()) {

            try {
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userName, password));

                String token = tokenService.generateJwt(auth);

                UserEntity userEntity = userOptional.get();
                UserDTO userDTO = userConverter.convertToDTO(userEntity);
                userDTO.setToken(token);
                return userDTO;
            }

            catch (AuthenticationException e) {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(
                        messageSource.getMessage("user.error.authentication.code", null,
                                LocaleContextHolder.getLocale()));
                errorModel.setMessage(messageSource.getMessage("user.error.authentication.message", null,
                        LocaleContextHolder.getLocale()));
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);
            }

        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("user.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("user.not_found.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

    }

}
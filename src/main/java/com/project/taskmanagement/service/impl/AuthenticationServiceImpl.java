package com.project.taskmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.converter.RoleConverter;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.RoleDTO;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.AuditEntity;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.repository.AuditRepository;
import com.project.taskmanagement.repository.RoleRepository;
import com.project.taskmanagement.repository.TableRegistryRepository;
import com.project.taskmanagement.repository.UserRepository;
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

    @Autowired
    private TableRegistryRepository tableRegistryRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Override
    public String createAdminUser(UserDTO userDTO) {

        Optional<UserEntity> existingUserByNameAndMail = userRepository.findByUserNameAndUserMail(userDTO.getUserName(),
                userDTO.getUserMail());
        if (existingUserByNameAndMail.isPresent()) {
            if (existingUserByNameAndMail.get().isActive()) {
                return messageSource.getMessage("user.account.exists", null,
                        LocaleContextHolder.getLocale());
            } else {
                existingUserByNameAndMail.get().setActive(true);

                String password = userDTO.getPassword();
                String encodedPassword = passwordEncoder.encode(password);
                existingUserByNameAndMail.get().setPassword(encodedPassword);
                existingUserByNameAndMail.get().setName(userDTO.getName());

                boolean adminRole = roleRepository.existsByDesignation("ADMIN");
                if (adminRole) {

                    Long adminRoleId = roleRepository.getRoleIdByDesignation("ADMIN").getRoleId();

                    RoleDTO adminDTO = new RoleDTO();
                    adminDTO.setRoleId(adminRoleId);

                    existingUserByNameAndMail.get().setRoleId(RoleConverter.convertToEntity(adminDTO));

                    LocalDateTime now = LocalDateTime.now();
                    existingUserByNameAndMail.get().setLastModifiedAt(now);

                    Optional<UserEntity> anonymousUserId = userRepository.findByUserName("anonymousUser");

                    existingUserByNameAndMail.get().setLastModifiedBy(anonymousUserId.get().getUserId());

                    userRepository.save(existingUserByNameAndMail.get());

                    String modifiedValue = existingUserByNameAndMail.get().toString();
                    AuditEntity auditEntity = new AuditEntity();
                    auditEntity.setModifiedValue(modifiedValue);
                    Long tableId = tableRegistryRepository.getTableIdByTableName("user_detail").getTableId();
                    auditEntity.setTableId(tableId);
                    auditEntity.setAction("update");
                    auditEntity.setLastModifiedAt(now);
                    auditEntity.setLastModifiedBy(anonymousUserId.get().getUserId());
                    auditRepository.save(auditEntity);

                    return messageSource.getMessage("user.activated", null, LocaleContextHolder.getLocale());
                } else {
                    return messageSource.getMessage("role.not_found.code", null,
                            LocaleContextHolder.getLocale());
                }

            }
        }

        Optional<UserEntity> existingUserByMail = userRepository.findByUserMail(userDTO.getUserMail());
        if (existingUserByMail.isPresent()) {
            return messageSource.getMessage("user.mail.exists", null,
                    LocaleContextHolder.getLocale());
        }

        Optional<UserEntity> existingUserByName = userRepository.findByUserName(userDTO.getUserName());
        if (existingUserByName.isPresent()) {
            return messageSource.getMessage("user.name.exists", null,
                    LocaleContextHolder.getLocale());
        }

        UserEntity newUser = UserConverter.convertToEntity(userDTO);
        newUser.setActive(true);

        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        Long adminRoleId = roleRepository.getRoleIdByDesignation("ADMIN").getRoleId();

        RoleDTO adminDTO = new RoleDTO();
        adminDTO.setRoleId(adminRoleId);

        newUser.setRoleId(RoleConverter.convertToEntity(adminDTO));

        LocalDateTime now = LocalDateTime.now();
        newUser.setCreatedAt(now);

        Optional<UserEntity> anonymousUserId = userRepository.findByUserName("anonymousUser");

        newUser.setCreatedBy(anonymousUserId.get().getUserId());

        userRepository.save(newUser);

        String modifiedValue = newUser.toString();
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setModifiedValue(modifiedValue);
        Long tableId = tableRegistryRepository.getTableIdByTableName("user_detail").getTableId();
        auditEntity.setTableId(tableId);
        auditEntity.setAction("create");
        auditRepository.save(auditEntity);

        return messageSource.getMessage("user.created", null, LocaleContextHolder.getLocale());

    }

    @Override
    public UserDTO loginAdminUser(String userName, String password) {

        Optional<UserEntity> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isPresent()) {

            if (userOptional.get().isActive()) {
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
                        messageSource.getMessage("user.condition.deactivated.code", null,
                                LocaleContextHolder.getLocale()));
                errorModel.setMessage(
                        messageSource.getMessage("user.condition.deactivated.message", null,
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
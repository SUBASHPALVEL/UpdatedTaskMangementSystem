package com.project.taskmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.AuditEntity;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.repository.AuditRepository;
import com.project.taskmanagement.repository.TableRegistryRepository;
import com.project.taskmanagement.repository.UserRepository;
import com.project.taskmanagement.service.CurrentUserService;
import com.project.taskmanagement.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TableRegistryRepository tableRegistryRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    public String createUser(UserDTO userDTO) {

        Optional<UserEntity> existingUserByNameAndMail = userRepository.findByUserNameAndUserMail(userDTO.getUserName(),
                userDTO.getUserMail());
        if (existingUserByNameAndMail.isPresent()) {
            if (existingUserByNameAndMail.get().isActive()) {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(
                        messageSource.getMessage("user.account.exists.code", null, LocaleContextHolder.getLocale()));
                errorModel.setMessage(
                        messageSource.getMessage("user.account.exists.message", null, LocaleContextHolder.getLocale()));
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);
            } else {
                existingUserByNameAndMail.get().setActive(true);

                String password = userDTO.getPassword();
                String encodedPassword = passwordEncoder.encode(password);
                existingUserByNameAndMail.get().setPassword(encodedPassword);
                existingUserByNameAndMail.get().setName(userDTO.getName());
                existingUserByNameAndMail.get().setRoleId(userDTO.getRoleId());

                LocalDateTime now = LocalDateTime.now();
                existingUserByNameAndMail.get().setLastModifiedAt(now);
                existingUserByNameAndMail.get().setLastModifiedBy(currentUserService.getCurrentUserId());

                userRepository.save(existingUserByNameAndMail.get());

                String modifiedValue = existingUserByNameAndMail.get().toString();
                AuditEntity auditEntity = new AuditEntity();
                auditEntity.setModifiedValue(modifiedValue);
                Long tableId = tableRegistryRepository.getTableIdByTableName("user_detail").getTableId();
                auditEntity.setTableId(tableId);
                auditEntity.setAction("update");
                auditEntity.setLastModifiedBy(currentUserService.getCurrentUserId());
                auditEntity.setLastModifiedAt(now);
                auditRepository.save(auditEntity);

                return messageSource.getMessage("user.activated", null, LocaleContextHolder.getLocale());
            }
        }

        Optional<UserEntity> existingUserByMail = userRepository.findByUserMail(userDTO.getUserMail());
        if (existingUserByMail.isPresent()) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("user.mail.exists.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("user.mail.exists.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        Optional<UserEntity> existingUserByName = userRepository.findByUserName(userDTO.getUserName());
        if (existingUserByName.isPresent()) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("user.name.exists.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("user.name.exists.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        UserEntity newUser = UserConverter.convertToEntity(userDTO);
        newUser.setActive(true);

        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        LocalDateTime now = LocalDateTime.now();
        newUser.setCreatedAt(now);
        newUser.setCreatedBy(currentUserService.getCurrentUserId());
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
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findByIsActiveTrue();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserEntity user : users) {
            UserDTO userDTO = UserConverter.convertToDTO(user);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            UserDTO userDTO = userConverter.convertToDTO(userEntity);
            return userDTO;

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

    @Override
    public String updateUser(Long userId, UserDTO userDTO) {

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {

            UserEntity user = optionalUser.get();

            if (!userDTO.getUserName().equals(user.getUsername())) {
                if (userRepository.existsByUserName(userDTO.getUserName())) {
                    List<ErrorModel> errorModelList = new ArrayList<>();
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.setCode(
                            messageSource.getMessage("user.name.exists.code", null, LocaleContextHolder.getLocale()));
                    errorModel.setMessage(
                            messageSource.getMessage("user.name.exists.message", null,
                                    LocaleContextHolder.getLocale()));
                    errorModelList.add(errorModel);
                    throw new BusinessException(errorModelList);
                }

                user.setUserName(userDTO.getUserName());
            }

            if (!userDTO.getUserMail().equals(user.getUserMail())) {

                if (userRepository.existsByUserMail(userDTO.getUserMail())) {

                    List<ErrorModel> errorModelList = new ArrayList<>();
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.setCode(
                            messageSource.getMessage("user.mail.exists.code", null, LocaleContextHolder.getLocale()));
                    errorModel.setMessage(
                            messageSource.getMessage("user.mail.exists.message", null,
                                    LocaleContextHolder.getLocale()));
                    errorModelList.add(errorModel);
                    throw new BusinessException(errorModelList);

                }

                user.setUserMail(userDTO.getUserMail());
            }

            user.setName(userDTO.getName());
            user.setRoleId(userDTO.getRoleId());

            LocalDateTime now = LocalDateTime.now();
            user.setLastModifiedAt(now);

            user.setLastModifiedBy(currentUserService.getCurrentUserId());
            userRepository.save(user);

            String modifiedValue = user.toString();
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setModifiedValue(modifiedValue);
            Long tableId = tableRegistryRepository.getTableIdByTableName("user_detail").getTableId();
            auditEntity.setTableId(tableId);
            auditEntity.setAction("update");
            auditEntity.setLastModifiedAt(now);
            auditEntity.setLastModifiedBy(currentUserService.getCurrentUserId());
            auditRepository.save(auditEntity);

            return messageSource.getMessage("user.updated", null, LocaleContextHolder.getLocale());
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

    @Override
    public String deleteUser(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setActive(false);
            LocalDateTime now = LocalDateTime.now();
            user.setLastModifiedAt(now);
            user.setLastModifiedBy(currentUserService.getCurrentUserId());
            userRepository.save(user);

            String modifiedValue = user.toString();
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setModifiedValue(modifiedValue);
            Long tableId = tableRegistryRepository.getTableIdByTableName("user_detail").getTableId();
            auditEntity.setTableId(tableId);
            auditEntity.setAction("delete");
            auditEntity.setLastModifiedAt(now);
            auditEntity.setLastModifiedBy(currentUserService.getCurrentUserId());
            auditRepository.save(auditEntity);

            return messageSource.getMessage("user.deleted", null, LocaleContextHolder.getLocale());
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

    @Override
    public String changePassword(UserDTO userDTO) {
        String userName = userDTO.getUserName();
        Optional<UserEntity> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isPresent()) {
            String decodedOldPasswordDTO = userDTO.getOldPassword();
            UserEntity user = userOptional.get();
            String encodedOldPasswordEntity = user.getPassword();
            if (passwordEncoder.matches(decodedOldPasswordDTO, encodedOldPasswordEntity)) {

                String password = userDTO.getNewPassword();
                String encodedPassword = passwordEncoder.encode(password);
                user.setPassword(encodedPassword);
                LocalDateTime now = LocalDateTime.now();
                user.setLastModifiedAt(now);
                user.setLastModifiedBy(currentUserService.getCurrentUserId());
                userRepository.save(user);

                String modifiedValue = user.toString();
                AuditEntity auditEntity = new AuditEntity();
                auditEntity.setModifiedValue(modifiedValue);
                Long tableId = tableRegistryRepository.getTableIdByTableName("user_detail").getTableId();
                auditEntity.setTableId(tableId);
                auditEntity.setAction("update");
                auditEntity.setLastModifiedBy(currentUserService.getCurrentUserId());
                auditEntity.setLastModifiedAt(now);
                auditRepository.save(auditEntity);

                return messageSource.getMessage("user.password.updated", null, LocaleContextHolder.getLocale());

            } else {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(
                        messageSource.getMessage("user.password.not_match.code", null,
                                LocaleContextHolder.getLocale()));
                errorModel.setMessage(
                        messageSource.getMessage("user.password.not_match.message", null,
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("user.mail.not_found", null, LocaleContextHolder.getLocale())));
    }

}

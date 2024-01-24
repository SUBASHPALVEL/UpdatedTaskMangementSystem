package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
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

    @Override
    public String createUser(UserDTO userDTO) {
        Optional<UserEntity> existingUser = userRepository.findByUserMail(userDTO.getUserMail());

        if (existingUser.isPresent()) {
            if (existingUser.get().isActive()) {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(messageSource.getMessage("user.exists.code", null, LocaleContextHolder.getLocale()));
                errorModel.setMessage(
                        messageSource.getMessage("user.exists.message", null, LocaleContextHolder.getLocale()));
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);

            } else {
                existingUser.get().setActive(true);
                userRepository.save(existingUser.get());
                return messageSource.getMessage("user.activated", null, LocaleContextHolder.getLocale());
            }
        } else {
            UserEntity newUser = UserConverter.convertToEntity(userDTO);
            newUser.setActive(true);

            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);

            userRepository.save(newUser);
            return messageSource.getMessage("user.created", null, LocaleContextHolder.getLocale());
        }
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
            // Alternative for mapper and converter
            // UserDTO userDTO = new UserDTO();
            // BeanUtils.copyProperties(userOptional.get(), userDTO);

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
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setUserName(userDTO.getUserName());
            user.setUserMail(userDTO.getUserMail());
            user.setRoleId(userDTO.getRoleId());
            userRepository.save(user);
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
            userRepository.save(user);
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
                userRepository.save(user);
                return messageSource.getMessage("user.password.updated", null, LocaleContextHolder.getLocale());

            } else {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(
                        messageSource.getMessage("user.password.not_match.code", null, LocaleContextHolder.getLocale()));
                errorModel.setMessage(
                        messageSource.getMessage("user.password.not_match.message", null, LocaleContextHolder.getLocale()));
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

        System.out.println("In the user details service");
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("user.mail.not_found", null, LocaleContextHolder.getLocale())));
    }

}

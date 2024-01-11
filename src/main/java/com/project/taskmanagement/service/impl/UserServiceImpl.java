package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Override
    public String createUser(UserDTO userDTO) {
        Optional<UserEntity> existingUser = userRepository.findByUserMail(userDTO.getUserMail());

        if (existingUser.isPresent()) {
            if (existingUser.get().isActive()) {
                List<ErrorModel> errorModelList = new ArrayList<>();
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode("USER_EXISTS");
                errorModel.setMessage("User Mail is already registered");
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);

            } else {
                existingUser.get().setActive(true);
                userRepository.save(existingUser.get());
                return "User is Re-registered and activated";
            }
        } else {
            UserEntity newUser = UserConverter.convertToEntity(userDTO);
            userRepository.save(newUser);
            return "User created successfully";
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findByIsActiveTrue();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserEntity user : users) {
            //
            //
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
            errorModel.setCode("USER_NOT_FOUND");
            errorModel.setMessage("User not found");
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
            user.setPassword(userDTO.getPassword());
            user.setRoleId(userDTO.getRoleId());
            userRepository.save(user);
            return "User updated successfully";
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
    public String deleteUser(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setActive(false);
            userRepository.save(user);
            return "User deleted successfully";
        } else {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("USER_NOT_FOUND");
            errorModel.setMessage("User not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
    }
}
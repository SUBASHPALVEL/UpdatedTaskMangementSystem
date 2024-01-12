package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.project.taskmanagement.service.TokenService;
import com.project.taskmanagement.service.UserService;


@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

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
            newUser.setActive(true);
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

    @Override
    public String createAdminUser(UserDTO userDTO) {
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
                RoleDTO adminDTO = new RoleDTO();
                adminDTO.setRoleId((long) 1);
                existingUser.get().setRoleId(RoleConverter.convertToEntity(adminDTO));
                userRepository.save(existingUser.get());
                return "User is Re-registered and activated";
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
            return "User created successfully";
        }
    }
    
    @Override
    public String loginAdminUser (String userMail, String password){


         try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userMail, password)
            );

            String token = tokenService.generateJwt(auth);

            return token;

        } catch(AuthenticationException e){
            return "Authentication error: " + e.getMessage();
        }

    }
}

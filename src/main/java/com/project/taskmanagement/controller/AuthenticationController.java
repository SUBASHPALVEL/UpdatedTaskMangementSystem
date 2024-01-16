package com.project.taskmanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.UserEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.AuthenticationService;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/admin")
    public ResponseEntity<String> createAdminUser(@RequestBody UserDTO userDTO) {
        try {
            String result = authenticationService.createAdminUser(userDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity <UserDTO> loginUser(@RequestBody UserDTO body){
            UserDTO user = authenticationService.loginAdminUser(body.getUserName(), body.getPassword());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } 

    }

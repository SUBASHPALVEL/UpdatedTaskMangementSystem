package com.project.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.UserService;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;
    

    @PostMapping("/admin")
    public ResponseEntity<String> createAdminUser(@RequestBody UserDTO userDTO) {
        try {
            String result = userService.createAdminUser(userDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/login")
    public String loginUser(@RequestBody UserDTO body){
        return userService.loginAdminUser(body.getName(), body.getPassword());
    }
}

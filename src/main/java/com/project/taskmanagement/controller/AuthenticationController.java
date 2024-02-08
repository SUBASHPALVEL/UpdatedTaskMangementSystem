package com.project.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.AuthenticationService;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/admin")
    public ResponseEntity<String> createAdminUser(@RequestBody UserDTO userDTO) {
        try {
            String result = authenticationService.createAdminUser(userDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO body) {
        try {
            UserDTO user = authenticationService.loginUser(body.getUserName(), body.getPassword());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }
}
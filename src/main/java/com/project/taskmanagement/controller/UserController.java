package com.project.taskmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.UserService;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        try {
            String result = userService.createUser(userDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        try {
            String result = userService.updateUser(userId, userDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            String result = userService.deleteUser(userId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody UserDTO userDTO){
        try {
            String result = userService.changePassword(userDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch(BusinessException bex){
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
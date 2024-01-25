package com.project.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanagement.dto.RoleDTO;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.RoleService;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            roleService.createRole(roleDTO);
            return new ResponseEntity<>("Role created successfully", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRoleByUserId(@PathVariable Long userId) {
        try {
            RoleDTO roleDTO = roleService.getRoleByUserId(userId);
            return new ResponseEntity<>(roleDTO, HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getErrorList().get(0).getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getErrorList().get(0).getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO) {
        try {
            roleService.updateRole(roleId, roleDTO);
            return new ResponseEntity<>("Role updated successfully", HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        try {
            roleService.deleteRole(roleId);
            return new ResponseEntity<>("Role deleted successfully", HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

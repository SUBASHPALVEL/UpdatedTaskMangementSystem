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

/**
 * Controller class for handling user-related HTTP requests.
 * 
 * @author Subash Palvel
 * @since 07/02/2024
 */

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint for creating a new user.
     * 
     * @param userDTO The UserDTO representing the new user to be created.
     * @return ResponseEntity containing a success message if the user was created
     *         successfully,
     *         or an error message if creation failed.
     */

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        try {
            String result = userService.createUser(userDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Endpoint for retrieving all users.
     * 
     * @return ResponseEntity containing a list of UserDTO objects representing all
     *         users,
     *         or an error message if retrieval failed.
     */

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {

        try {
            List<UserDTO> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Endpoint for retrieving a user by ID.
     * 
     * @param userId The ID of the user to retrieve.
     * @return ResponseEntity containing a UserDTO representing the requested user,
     *         or an error message if retrieval failed.
     */

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {

        try {
            UserDTO user = userService.getUserById(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Endpoint for updating an existing user.
     * 
     * @param userId  The ID of the user to update.
     * @param userDTO The updated UserDTO representing the changes to be made.
     * @return ResponseEntity containing a success message if the user was updated
     *         successfully,
     *         or an error message if update failed.
     */

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        try {
            String result = userService.updateUser(userId, userDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for deleting a user by ID.
     * 
     * @param userId The ID of the user to delete.
     * @return ResponseEntity containing a success message if the user was deleted
     *         successfully,
     *         or an error message if deletion failed.
     */

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            String result = userService.deleteUser(userId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for changing a user's password.
     * @param userDTO The UserDTO containing the user's ID and new password.
     * @return ResponseEntity containing a success message if the password was changed successfully,
     *         or an error message if the operation failed.
     */

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody UserDTO userDTO) {
        try {
            String result = userService.changePassword(userDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
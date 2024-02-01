package com.project.taskmanagement.controller;

import java.util.List;

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

import com.project.taskmanagement.dto.TaskDTO;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.TaskService;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping()
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO) {
        try {
            String result = taskService.createTask(taskDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllTasks() {

        try {
            List<TaskDTO> tasks = taskService.getAllTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {

        try {
            TaskDTO task = taskService.getTaskById(taskId);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUserId(@PathVariable Long userId) {

        try {
            List<TaskDTO> tasks = taskService.getTasksByUserId(userId);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        try {
            String result = taskService.updateTask(taskId, taskDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        try {
            String result = taskService.deleteTask(taskId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
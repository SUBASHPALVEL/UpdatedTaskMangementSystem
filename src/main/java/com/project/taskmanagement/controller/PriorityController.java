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

import com.project.taskmanagement.dto.PriorityDTO;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.PriorityService;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/priorities")
public class PriorityController {

    @Autowired
    private PriorityService priorityService;

    @PostMapping
    public ResponseEntity<String> createPriority(@RequestBody PriorityDTO priorityDTO) {
        try {
            String createdPriority = priorityService.createPriority(priorityDTO);
            return new ResponseEntity<>(createdPriority, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPriorities() {

        try {
            List<PriorityDTO> priorities = priorityService.getAllPriorities();
            return new ResponseEntity<>(priorities, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{priorityId}")
    public ResponseEntity<String> updatePriority(@PathVariable Long priorityId, @RequestBody PriorityDTO priorityDTO) {
        try {
            String updatedPriority = priorityService.updatePriority(priorityId, priorityDTO);
            return new ResponseEntity<>(updatedPriority, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{priorityId}")
    public ResponseEntity<String> deletePriority(@PathVariable Long priorityId) {
        try {
            priorityService.deletePriority(priorityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
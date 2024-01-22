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
        } catch (BusinessException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<PriorityDTO>> getAllPriorities() {
        List<PriorityDTO> priorities = priorityService.getAllPriorities();
        return new ResponseEntity<>(priorities, HttpStatus.OK);
    }

    @PutMapping("/{priorityId}")
    public ResponseEntity<String> updatePriority(@PathVariable Long priorityId, @RequestBody PriorityDTO priorityDTO) {
        try {
            String updatedPriority = priorityService.updatePriority(priorityId, priorityDTO);
            return new ResponseEntity<>(updatedPriority, HttpStatus.OK);

        } catch (BusinessException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{priorityId}")
    public ResponseEntity<String> deletePriority(@PathVariable Long priorityId) {
        try {
            priorityService.deletePriority(priorityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BusinessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
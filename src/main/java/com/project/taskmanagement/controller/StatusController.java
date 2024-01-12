package com.project.taskmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanagement.dto.StatusDTO;
import com.project.taskmanagement.service.StatusService;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @PostMapping
    public ResponseEntity<String> createStatus(@RequestBody StatusDTO statusDTO) {
        String createdStatus = statusService.createStatus(statusDTO);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StatusDTO>> getAllStatus() {
        List<StatusDTO> allStatus = statusService.getAllStatus();
        return new ResponseEntity<>(allStatus, HttpStatus.OK);
    }

    @PutMapping("/{statusId}")
    public ResponseEntity<String> updateStatus(@PathVariable Long statusId, @RequestBody StatusDTO statusDTO) {
        String updatedStatus = statusService.updateStatus(statusId, statusDTO);
        return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
    }

    @DeleteMapping("/{statusId}")
    public ResponseEntity<String> deleteStatus(@PathVariable Long statusId) {
        statusService.deleteStatus(statusId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

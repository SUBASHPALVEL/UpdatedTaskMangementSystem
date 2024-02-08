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

import com.project.taskmanagement.dto.StatusDTO;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.StatusService;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @PostMapping
    public ResponseEntity<String> createStatus(@RequestBody StatusDTO statusDTO) {

        try {
            String createdStatus = statusService.createStatus(statusDTO);
            return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllStatus() {

        try {
            List<StatusDTO> allStatus = statusService.getAllStatus();
            return new ResponseEntity<>(allStatus, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{statusId}")
    public ResponseEntity<String> updateStatus(@PathVariable Long statusId, @RequestBody StatusDTO statusDTO) {

        try {
            String updatedStatus = statusService.updateStatus(statusId, statusDTO);
            return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
        } catch (BusinessException bex) {
            return new ResponseEntity<>(bex.getErrorList().get(0).getMessage(),
                    HttpStatus.valueOf(bex.getErrorList().get(0).getCode()));
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{statusId}")
    public ResponseEntity<String> deleteStatus(@PathVariable Long statusId) {

        try {
            statusService.deleteStatus(statusId);
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

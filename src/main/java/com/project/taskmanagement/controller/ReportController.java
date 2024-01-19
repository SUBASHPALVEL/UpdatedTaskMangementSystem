package com.project.taskmanagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.service.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;
    
    @GetMapping("/task-vs-assign")
    public ResponseEntity<Map<String, Object>> getTaskVsAssignReport() {
        try {
            Map<String, Object> result = reportService.getTaskVsAssignReport();
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}

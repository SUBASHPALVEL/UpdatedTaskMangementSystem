package com.project.taskmanagement.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.dto.TaskDTO;
import com.project.taskmanagement.dto.UserDTO;
import com.project.taskmanagement.entity.TaskEntity;
import com.project.taskmanagement.service.ReportService;
import com.project.taskmanagement.service.TaskService;
import com.project.taskmanagement.service.UserService;

@Service
public class ReportServiceImpl implements ReportService{
    


    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    


    @Override
    public Map<String, Object> getTaskVsAssignReport(){
        List<TaskDTO> allTasks = taskService.getAllTasks();
        List<UserDTO> allUsers = userService.getAllUsers();

        long totalTasks = allTasks.size();
        long totalUsers = allUsers.size();
        long totalAssignments = 0;

        for(TaskDTO task : allTasks){
            totalAssignments += task.getAssignedUsers().size();
        }

        double averageAssignmentPerTask = (totalTasks > 0) ? (double) totalAssignments/totalTasks : (double) 0; 
        double averageTaskPerUser = (totalUsers > 0 ) ? (double) totalTasks/totalUsers : (double) 0;

        Map<String, Object> report = Map.of(
            "totalTasks", totalTasks,
            "totalUsers", totalUsers,
            "totalAssignments", totalAssignments,
            "averageTasksPerUser", averageTaskPerUser,
            "averageAssignmentPerTask ", averageAssignmentPerTask 


        );
        return report;
    }
}

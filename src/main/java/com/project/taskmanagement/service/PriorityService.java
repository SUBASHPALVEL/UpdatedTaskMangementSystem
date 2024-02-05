package com.project.taskmanagement.service;

import java.util.List;

import com.project.taskmanagement.dto.PriorityDTO;

public interface PriorityService {

    String createPriority(PriorityDTO priorityDTO);

    List<PriorityDTO> getAllPriorities();

    String updatePriority(Long priorityId, PriorityDTO priorityDTO);

    String deletePriority(Long priorityId);
}

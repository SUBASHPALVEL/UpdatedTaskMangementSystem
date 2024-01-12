package com.project.taskmanagement.service;

import java.util.List;

import com.project.taskmanagement.dto.StatusDTO;

public interface StatusService {

    String createStatus(StatusDTO statusDTO);

    List<StatusDTO> getAllStatus();

    String updateStatus(Long statusId, StatusDTO statusDTO);

    String deleteStatus(Long statusId);
}


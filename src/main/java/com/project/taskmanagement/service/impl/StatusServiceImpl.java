package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.converter.StatusConverter;
import com.project.taskmanagement.dto.StatusDTO;
import com.project.taskmanagement.entity.StatusEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.repository.StatusRepository;
import com.project.taskmanagement.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String createStatus(StatusDTO statusDTO) {

        if (statusRepository.existsByStatusLevel(statusDTO.getStatusLevel())) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("status.exists.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(messageSource.getMessage("status.exists.message", null,
                    LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setStatusLevel(statusDTO.getStatusLevel());
        statusRepository.save(statusEntity);
        return messageSource.getMessage("status.created", null, LocaleContextHolder.getLocale());

    }

    @Override
    public List<StatusDTO> getAllStatus() {
        List<StatusEntity> allStatusEntities = statusRepository.findAll();
        List<StatusDTO> allStatus = new ArrayList<>();
        for (StatusEntity statusEntity : allStatusEntities) {
            StatusDTO statusDTO = StatusConverter.convertToDTO(statusEntity);
            allStatus.add(statusDTO);
        }
        return allStatus;
    }

    @Override
    public String updateStatus(Long statusId, StatusDTO statusDTO) {

        if (!statusRepository.existsById(statusId)) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("status.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(messageSource.getMessage("status.not_found.message", null,
                    LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setStatusId(statusId);
        statusEntity.setStatusLevel(statusDTO.getStatusLevel());
        statusRepository.save(statusEntity);
        return messageSource.getMessage("status.updated", null, LocaleContextHolder.getLocale());

    }

    @Override
    public String deleteStatus(Long statusId) {

        if (!statusRepository.existsById(statusId)) {

            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("status.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(messageSource.getMessage("status.not_found.message", null,
                    LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        statusRepository.deleteById(statusId);
        return messageSource.getMessage("status.deleted", null, LocaleContextHolder.getLocale());
    }
}
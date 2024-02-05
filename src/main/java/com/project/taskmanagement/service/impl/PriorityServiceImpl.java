package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.converter.PriorityConverter;
import com.project.taskmanagement.dto.PriorityDTO;
import com.project.taskmanagement.entity.PriorityEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.repository1.PriorityRepository;
import com.project.taskmanagement.service.PriorityService;

@Service
public class PriorityServiceImpl implements PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String createPriority(PriorityDTO priorityDTO) throws BusinessException {

        if (priorityRepository.existsByPriorityStatus(priorityDTO.getPriorityStatus())) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(messageSource.getMessage("priority.exists.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("priority.exists.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setPriorityId(priorityDTO.getPriorityId());
        priorityEntity.setPriorityStatus(priorityDTO.getPriorityStatus());
        priorityRepository.save(priorityEntity);
        return messageSource.getMessage("priority.created", null, LocaleContextHolder.getLocale());

    }

    @Override
    public List<PriorityDTO> getAllPriorities() {

        List<PriorityEntity> priorityEntities = priorityRepository.findAll();
        List<PriorityDTO> priorityDTOs = new ArrayList<>();
        for (PriorityEntity priorityEntity : priorityEntities) {
            PriorityDTO priorityDTO = PriorityConverter.convertToDTO(priorityEntity);
            priorityDTOs.add(priorityDTO);
        }
        return priorityDTOs;

    }

    @Override
    public String updatePriority(Long priorityId, PriorityDTO priorityDTO) throws BusinessException {

        if (!priorityRepository.existsById(priorityId)) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("priority.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("priority.not_found.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setPriorityId(priorityId);
        priorityEntity.setPriorityStatus(priorityDTO.getPriorityStatus());
        priorityRepository.save(priorityEntity);
        return messageSource.getMessage("priority.updated", null, LocaleContextHolder.getLocale());

    }

    @Override
    public String deletePriority(Long priorityId) throws BusinessException {

        if (!priorityRepository.existsById(priorityId)) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(
                    messageSource.getMessage("priority.not_found.code", null, LocaleContextHolder.getLocale()));
            errorModel.setMessage(
                    messageSource.getMessage("priority.not_found.message", null, LocaleContextHolder.getLocale()));
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
        priorityRepository.deleteById(priorityId);
        return messageSource.getMessage("priority.deleted", null, LocaleContextHolder.getLocale());

    }
}

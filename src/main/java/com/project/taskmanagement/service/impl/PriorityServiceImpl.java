package com.project.taskmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.Repository.PriorityRepository;
import com.project.taskmanagement.converter.PriorityConverter;
import com.project.taskmanagement.dto.PriorityDTO;
import com.project.taskmanagement.entity.PriorityEntity;
import com.project.taskmanagement.exception.BusinessException;
import com.project.taskmanagement.exception.ErrorModel;
import com.project.taskmanagement.service.PriorityService;

@Service
public class PriorityServiceImpl implements PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    @Override
    public String createPriority(PriorityDTO priorityDTO) throws BusinessException {

        if (priorityRepository.existsByPriorityStatus(priorityDTO.getPriorityStatus())) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("PRIORITY_STATUS_ALREADY_EXIST");
            errorModel.setMessage("Priority Status already exists");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setPriorityId(priorityDTO.getPriorityId());
        priorityEntity.setPriorityStatus(priorityDTO.getPriorityStatus());
        priorityRepository.save(priorityEntity);
        return "Priority Status created successfully";

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
            errorModel.setCode("PRIORITY_ID_FOUND");
            errorModel.setMessage("Priority ID not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setPriorityId(priorityId);
        priorityEntity.setPriorityStatus(priorityDTO.getPriorityStatus());
        priorityRepository.save(priorityEntity);
        return "Priority Status updated Successfully";

    }

    @Override
    public String deletePriority(Long priorityId) throws BusinessException {

        if (!priorityRepository.existsById(priorityId)) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode("PRIORITY_STATUS_NOT_FOUND");
            errorModel.setMessage("Priority ID not found");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
        priorityRepository.deleteById(priorityId);
        return "Priority Status deleted successfully";

    }
}

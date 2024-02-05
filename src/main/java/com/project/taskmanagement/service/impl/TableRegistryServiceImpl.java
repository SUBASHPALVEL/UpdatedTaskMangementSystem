package com.project.taskmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.taskmanagement.entity.TableRegistry;
import com.project.taskmanagement.repository.TableRegistryRepository;
import com.project.taskmanagement.service.TableRegistryService;

@Service
public class TableRegistryServiceImpl implements TableRegistryService {

    @Autowired
    private TableRegistryRepository tableRegistryRepository;

    @Override
    public void storeTableName(String tableName) {

        if (!tableRegistryRepository.existsByTableName(tableName)) {
            TableRegistry tableRegistry = new TableRegistry();
            tableRegistry.setTableName(tableName);
            tableRegistryRepository.save(tableRegistry);
        }
    }
}

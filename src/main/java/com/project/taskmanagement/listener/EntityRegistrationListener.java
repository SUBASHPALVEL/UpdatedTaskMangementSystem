package com.project.taskmanagement.listener;

import com.project.taskmanagement.service.TableRegistryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;

import com.project.taskmanagement.Repository.TableRegistryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component

public class EntityRegistrationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TableRegistryRepository tableRegistryRepository;

    @Autowired
    private TableRegistryService tableRegistryService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        storeTableNamesInTableRegistry();
    }

    private void storeTableNamesInTableRegistry() {
        for (EntityType<?> entityType : entityManager.getMetamodel().getEntities()) {
            Class<?> javaType = entityType.getJavaType();

            // Check if the class has the @Table annotation
            if (javaType.isAnnotationPresent(Table.class)) {
                Table tableAnnotation = javaType.getAnnotation(Table.class);
                String tableName = tableAnnotation.name();

                if (!tableRegistryRepository.existsByTableName(tableName)) {
                    System.out.println(tableName );
                    tableRegistryService.storeTableName(tableName);
                }
            }
        }
    }
}
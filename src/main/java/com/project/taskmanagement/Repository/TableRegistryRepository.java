package com.project.taskmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.TableRegistry;

@Repository
public interface TableRegistryRepository extends JpaRepository<TableRegistry, Long> {
    Boolean existsByTableName(String tableName);

    TableRegistry getTableIdByTableName(String tableName);
}

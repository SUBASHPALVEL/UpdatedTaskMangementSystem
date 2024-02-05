package com.project.taskmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class AuditDTO {

    private long logId;

    private Long tableId;

    private String modifiedValue;

    private String action;

    private Long lastModifiedBy;

    private LocalDateTime lastModifiedAt;
}

package com.project.taskmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="roles")
public class RoleEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long roleId;

    @Column(name="designation", nullable = false)
    private String designation;

}

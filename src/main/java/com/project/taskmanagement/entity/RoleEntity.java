package com.project.taskmanagement.entity;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Size(min = 2, max = 50, message = "Designation must be between 02 and 50 of size")
    @Column(name = "designation", nullable = false, unique = true)
    private String designation;

    public RoleEntity() {
        super();
    }

    public RoleEntity(Long roleId, String designation) {
        this.roleId = roleId;
        this.designation = designation;
    }

    @Override
    public String getAuthority() {

        return getDesignation();
    }

    public void setAuthority(String authority){
        this.designation = authority;
    }

    public RoleEntity(String designation) {
        this.designation = designation;
    }

    public RoleEntity(Long roleId) {
        this.roleId = roleId;
    }

}

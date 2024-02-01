package com.project.taskmanagement.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Setter
@Getter
@Entity
@Table(name = "user_detail")

@EntityListeners(AuditingEntityListener.class)

@Audited
@AuditTable(value = "user_detail_audit")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Size(min =3,max=100, message = "Name must be between 03 and 100 of size")
    @Column(name= "name", nullable = false)
    private String name;

    @Size(min = 2, max = 50, message = "User Name must be between 02 and 50 of size")
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Size(min = 2, max = 150, message = "User Mail must be between 02 and 150 of size")
    @Email(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,3}" , message = "Invalid email address")
    @Column(name = "user_mail", nullable = false, unique = true)
    private String userMail;

    @Size(min = 2, max = 60, message = "Password must be between 02 and 60 of size")
    @Column(name = "password", nullable = false)
    private String password;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "designation", nullable = false)
    private RoleEntity roleId;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @NotAudited
    @CreatedBy
    @Column(name = "created_by", updatable = false )
    private Long createdBy;

    @NotAudited
    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by" , insertable = false)
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at" , insertable = false)
    private LocalDateTime lastModifiedAt;

    @NotAudited
    @ManyToMany(mappedBy = "assignedUsers")
    private List<TaskEntity> assignedTasks = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(getRoleId().getDesignation()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserEntity() {
        super();
    }

    public UserEntity(Long userId, String username, String password, RoleEntity roleId) {
        super();
        this.userId = userId;
        this.userName = username;
        this.password = password;
        this.roleId = roleId;
    }

    public UserEntity(Long userId, String username, String password, RoleEntity roleId, String userMail) {
        super();
        this.userId = userId;
        this.userName = username;
        this.password = password;
        this.roleId = roleId;
        this.userMail = userMail;
    }
}

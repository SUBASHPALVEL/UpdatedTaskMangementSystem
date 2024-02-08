package com.project.taskmanagement.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a user.
 * @author Subash Palvel
 * @since 07/02/2024
 */

@Setter
@Getter
@Entity
@Table(name = "user_detail")

public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Size(min = 3, max = 100, message = "Name must be between 03 and 100 of size")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 2, max = 50, message = "User Name must be between 02 and 50 of size")
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Size(min = 2, max = 150, message = "User Mail must be between 02 and 150 of size")
    @Email(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,3}", message = "Invalid email address")
    @Column(name = "user_mail", nullable = false, unique = true)
    private String userMail;

    @Size(min = 2, max = 60, message = "Password must be between 02 and 60 of size")
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "designation", nullable = false)
    private RoleEntity roleId;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @ManyToMany(mappedBy = "assignedUsers")
    private List<TaskEntity> assignedTasks = new ArrayList<>();

    /**
     * Returns a string representation of the UserEntity object.
     *
     * @return A string containing user details.
     */

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", name=" + name +
                ", userName=" + userName +
                ", userMail=" + userMail +
                ", roleId=" + roleId.getRoleId() +
                ", isActive=" + isActive +
                '}';
    }

    /**
     * Returns the granted authorities.
     *
     * @return A List of GrantedAuthority objects.
     */

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

}

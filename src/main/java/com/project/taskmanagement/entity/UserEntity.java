package com.project.taskmanagement.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_detail")
public class UserEntity implements UserDetails  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Size(min=2, max=50,message = "User Name must be between 02 and 50 of size")
    @Column(name = "user_name", nullable = false,unique = true)
    private String userName;

    @Size(min=2, max=150,message = "User Mail must be between 02 and 150 of size")
    @Column(name = "user_mail", nullable = false,unique = true)
    private String userMail;

    @Size(min=2, max=50,message = "Password must be between 02 and 50 of size")
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name="designation")
    private RoleEntity roleId;

    @Column(name="is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @ManyToMany(mappedBy = "assignedUsers")
    private List<TaskEntity> assignedTasks = new ArrayList<>();






    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + getRoleId().getDesignation()));

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
    
}

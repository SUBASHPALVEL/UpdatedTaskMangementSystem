package com.project.taskmanagement.entity;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_detail")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "usermail", nullable = false)
    private String usermail;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name="role", nullable = false)
    private RoleEntity role;


    @ManyToMany(mappedBy = "assignedUsers")
    private List<TaskEntity> assignedTasks = new ArrayList<>();
    
}

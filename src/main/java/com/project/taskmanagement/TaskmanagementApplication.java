package com.project.taskmanagement;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.taskmanagement.Repository.RoleRepository;
import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.entity.RoleEntity;
import com.project.taskmanagement.entity.UserEntity;

@SpringBootApplication
public class TaskmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagementApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder){
		return args ->{
			if(roleRepository.findByDesignation("ADMIN").isPresent()) return;

			roleRepository.save(new RoleEntity(("ADMIN")));
			roleRepository.save(new RoleEntity("USER"));


			RoleEntity role = new RoleEntity((long) 1,"ADMIN");
        	UserEntity admin = new UserEntity ((long)1,"suba",encoder.encode("password"),role,"S@g.com");

			userRepository.save(admin);
		};
	}

}

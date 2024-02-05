package com.project.taskmanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.taskmanagement.Repository.RoleRepository;
import com.project.taskmanagement.Repository.UserRepository;
import com.project.taskmanagement.converter.RoleConverter;
import com.project.taskmanagement.converter.UserConverter;
import com.project.taskmanagement.dto.RoleDTO;
import com.project.taskmanagement.entity.RoleEntity;
import com.project.taskmanagement.entity.UserEntity;

import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class TaskmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagementApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			if (roleRepository.findByDesignation("ADMIN").isPresent())
				return;

			roleRepository.save(new RoleEntity("ADMIN"));

			Long adminRoleId = roleRepository.getRoleIdByDesignation("ADMIN").getRoleId();

            RoleDTO adminDTO = new RoleDTO();
            adminDTO.setRoleId(adminRoleId);

			RoleEntity role = RoleConverter.convertToEntity(adminDTO);
			
			UserEntity anonymousUser = new UserEntity( "anonymousUser", encoder.encode("password"), role, "anonymousUser@g.com", "anonymousUser");

			userRepository.save(anonymousUser);
		};
	}

}

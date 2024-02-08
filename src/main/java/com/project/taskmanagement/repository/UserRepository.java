package com.project.taskmanagement.repository;

import org.springframework.stereotype.Repository;

import com.project.taskmanagement.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing user entities.
 * @author Subash Palvel
 * @since 07/02/2024
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds a user by their email address.
     * 
     * @param userMail The email address of the user to find.
     * @return An Optional containing the user entity if found, or empty if not
     *         found.
     */

    Optional<UserEntity> findByUserMail(String userMail);

    /**
     * Retrieves a list of active users.
     * 
     * @return A list of active user entities.
     */

    List<UserEntity> findByIsActiveTrue();

    /**
     * Finds a user by their username.
     * 
     * @param userName The username of the user to find.
     * @return An Optional containing the user entity if found, or empty if not
     *         found.
     */

    Optional<UserEntity> findByUserName(String userName);

    /**
     * Finds a user by their username and email address.
     * 
     * @param userName The username of the user to find.
     * @param userMail The email address of the user to find.
     * @return An Optional containing the user entity if found, or empty if not
     *         found.
     */

    Optional<UserEntity> findByUserNameAndUserMail(String userName, String userMail);

    /**
     * Checks if a user with the given ID exists and is active.
     * 
     * @param userId The ID of the user to check.
     * @return True if a user with the given ID exists and is active, otherwise
     *         false.
     */

    boolean existsByUserIdAndIsActiveTrue(Long userId);

    /**
     * Finds a user by their ID.
     * 
     * @param userId The ID of the user to find.
     * @return An Optional containing the user entity if found, or empty if not
     *         found.
     */

    Optional<UserEntity> findByUserId(Long userId);

    /**
     * Checks if a user with the given username exists.
     * 
     * @param username The username to check.
     * @return True if a user with the given username exists, otherwise false.
     */

    Boolean existsByUserName(String username);

    /**
     * Checks if a user with the given email address exists.
     * 
     * @param userMail The email address to check.
     * @return True if a user with the given email address exists, otherwise false.
     */

    Boolean existsByUserMail(String userMail);

}

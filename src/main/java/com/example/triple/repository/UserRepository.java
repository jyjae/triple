package com.example.triple.repository;

import com.example.triple.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByIdAndStatus(UUID userId, String active);

    boolean existsByIdAndStatus(UUID userId, String active);
}

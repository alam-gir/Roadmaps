package com.roadmaps.Roadmaps.modules.user.repository;

import com.roadmaps.Roadmaps.modules.user.enities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);
}

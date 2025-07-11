package com.roadmaps.Roadmaps.modules.roadmap.repository;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByNameIgnoreCase(String name);
}

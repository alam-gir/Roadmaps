package com.roadmaps.Roadmaps.modules.roadmap.repository;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}

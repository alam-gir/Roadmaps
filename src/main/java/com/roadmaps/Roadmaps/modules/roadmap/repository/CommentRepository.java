package com.roadmaps.Roadmaps.modules.roadmap.repository;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> getCommentsByParentId(UUID commentId);

    Optional<Comment> findByIdAndUser_Email(UUID commentUuid, String email);
}

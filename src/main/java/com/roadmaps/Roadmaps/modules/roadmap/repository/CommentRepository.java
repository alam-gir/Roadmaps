package com.roadmaps.Roadmaps.modules.roadmap.repository;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> getCommentsByParentId(UUID commentId);

    Optional<Comment> findByIdAndUser_Email(UUID commentUuid, String email);

    Page<Comment> findAllByParent(Comment comment, Pageable pageable);

    Page<Comment> findAllByRoadmapAndParentIsNull(Roadmap roadmap, Pageable pageable);
}

package com.roadmaps.Roadmaps.modules.roadmap.service;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.CommentRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Page<Comment> getRootCommentsByRoadmapId(String roadmapId, Pageable pageable);
    Page<Comment> getRepliesByCommentId(String commentId, Pageable pageable);

    Comment getById(String commentId);
    Comment getById(UUID commentId);

    Comment addComment(String userEmail, UUID roadmapId, CommentRequestDto commentDto);
    Comment addCommentReply(String userEmail, UUID roadmapId, UUID parentCommentId, CommentRequestDto commentDto);

    void deleteUserComment(String email, String commentId);

    List<Comment> getAllNestedComments(UUID commentId);
    List<String> getAllImagesFromComments(List<Comment> comments);
}

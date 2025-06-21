package com.roadmaps.Roadmaps.modules.roadmap.service;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.CommentRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment getById(String commentId);
    Comment getById(UUID commentId);

    Comment addComment(String userEmail, UUID roadmapId, CommentRequestDto commentDto);
    Comment addCommentReply(String userEmail, UUID roadmapId, UUID parentCommentId, CommentRequestDto commentDto);

    void deleteById(String commentId);

    void deleteUserComment(String email, String commentId);

    List<Comment> getAllNestedComments(UUID commentId);

    void delete(Comment comment);
}

package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.CommentReplyResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.CommentResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment toCommentEntity(User user, Roadmap roadmap, Comment parent, String text, String image) {
        return Comment.builder()
                .user(user)
                .roadmap(roadmap)
                .parent(parent)
                .text(text)
                .image(image)
                .build();
    }

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        long totalUpvote = comment.getUpvotes() != null ? comment.getUpvotes().size() : 0;
        long totalReplies = comment.getChildren() != null ? comment.getChildren().size() : 0;

        return new CommentResponseDto(
                comment.getId().toString(),
                comment.getUser().getId().toString(),
                comment.getUser().getName(),
                comment.getText(),
                comment.getImage(),
                totalUpvote,
                totalReplies,
                comment.getCreatedAt()
        );
    }

    public CommentReplyResponseDto toCommentReplyResponseDto(Comment comment) {
        long totalUpvote = comment.getUpvotes() != null ? comment.getUpvotes().size() : 0;
        long totalReplies = comment.getChildren() != null ? comment.getChildren().size() : 0;

        return new CommentReplyResponseDto(
                comment.getId().toString(),
                comment.getUser().getId().toString(),
                comment.getUser().getName(),
                comment.getParent().getId().toString(),
                comment.getParent().getUser().getName(),
                comment.getText(),
                comment.getImage(),
                totalUpvote,
                totalReplies,
                comment.getCreatedAt()
        );
    }
}

package com.roadmaps.Roadmaps.modules.roadmap.dtos.response;

import java.time.LocalDateTime;

public record CommentReplyResponseDto(
        String id,
        String authorId,
        String author,
        String parentId,
        String parentAuthor,
        String text,
        String image,
        long totalUpvote,
        long totalReplies,
        LocalDateTime createdAt
){
}

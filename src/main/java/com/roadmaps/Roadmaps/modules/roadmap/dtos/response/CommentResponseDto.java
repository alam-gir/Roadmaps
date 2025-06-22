package com.roadmaps.Roadmaps.modules.roadmap.dtos.response;

import java.time.LocalDateTime;

public record CommentResponseDto (
        String id,
        String authorId,
        String author,
        String text,
        String image,
        long totalUpvote,
        long totalReplies,
        LocalDateTime createdAt
){
}

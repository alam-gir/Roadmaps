package com.roadmaps.Roadmaps.modules.roadmap.dtos.response;

public record CommentReplyResponseDto(
        String id,
        String parentId,
        String text,
        String image
){
}

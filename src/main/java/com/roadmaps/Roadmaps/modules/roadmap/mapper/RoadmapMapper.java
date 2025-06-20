package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.CommentReplyResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.CommentResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.RoadmapResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.UpvoteResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoadmapMapper {
    public Roadmap toEntity(RoadmapRequestDto roadmapDto, String image) {
        return Roadmap.builder()
                .text(roadmapDto.getText())
                .image(image)
                .build();
    }

    public RoadmapResponseDto toResponseDto(Roadmap roadmap) {
        return new RoadmapResponseDto(
                roadmap.getId().toString(),
                roadmap.getText(),
                roadmap.getImage()
        );
    }

    public List<UpvoteResponseDto> toUpvoteResponseDtoList(List<Upvote> upvotes) {
        return upvotes.stream().map(upvote -> new UpvoteResponseDto(upvote.getUser().getName())).toList();
    };

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
        return new CommentResponseDto(
                comment.getId().toString(),
                comment.getText(),
                comment.getImage()
        );
    }

    public CommentReplyResponseDto toCommentReplyResponseDto(Comment comment, String parentId) {
        return new CommentReplyResponseDto(
                comment.getId().toString(),
                parentId,
                comment.getText(),
                comment.getImage()
        );
    }
}

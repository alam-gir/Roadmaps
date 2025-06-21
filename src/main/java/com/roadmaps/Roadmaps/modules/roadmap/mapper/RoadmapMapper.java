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
}

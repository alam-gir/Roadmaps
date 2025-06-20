package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.RoadmapResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.UpvoteResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
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
}

package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.RoadmapResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import org.springframework.stereotype.Component;

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

package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.RoadmapResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Category;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.enumeration.ROADMAP_STATUS;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoadmapMapper {
    public Roadmap toEntity(RoadmapRequestDto roadmapDto, Category category, ROADMAP_STATUS status, String image) {
        return Roadmap.builder()
                .text(roadmapDto.getText())
                .image(image)
                .category(category)
                .status(status)
                .build();
    }

    public RoadmapResponseDto toResponseDto(Roadmap roadmap) {
        return new RoadmapResponseDto(
                roadmap.getId().toString(),
                roadmap.getText(),
                roadmap.getImage(),
                roadmap.getCategory().getName(),
                roadmap.getStatus(),
                roadmap.getCreatedAt()
        );
    }

    public List<RoadmapResponseDto> toResponseDtoList(List<Roadmap> roadmaps) {
        return roadmaps.stream().map(this::toResponseDto).toList();
    }
}

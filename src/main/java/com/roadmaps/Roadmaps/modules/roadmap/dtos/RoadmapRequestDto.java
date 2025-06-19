package com.roadmaps.Roadmaps.modules.roadmap.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RoadmapRequestDto {
    private String text;

    private MultipartFile image;
}

package com.roadmaps.Roadmaps.modules.roadmap.dtos.response;

import com.roadmaps.Roadmaps.modules.roadmap.enumeration.ROADMAP_STATUS;

import java.time.LocalDateTime;

public record RoadmapResponseDto (
        String id,
        String text,
        String image,
        String category,
        ROADMAP_STATUS status,
        LocalDateTime createdAt
){ }

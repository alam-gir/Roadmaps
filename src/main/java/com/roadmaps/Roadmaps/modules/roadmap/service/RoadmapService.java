package com.roadmaps.Roadmaps.modules.roadmap.service;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;

import java.util.UUID;

public interface RoadmapService {
    Roadmap getById(String id);
    Roadmap getById(UUID id);
    Roadmap addRoadmap(RoadmapRequestDto roadmapDto);

    void deleteById(String id);

}

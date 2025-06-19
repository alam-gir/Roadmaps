package com.roadmaps.Roadmaps.modules.roadmap.service;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;

public interface RoadmapService {
    Roadmap addRoadmap(RoadmapRequestDto roadmapDto);
}

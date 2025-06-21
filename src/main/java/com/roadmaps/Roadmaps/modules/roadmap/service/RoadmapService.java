package com.roadmaps.Roadmaps.modules.roadmap.service;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestFiltersDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RoadmapService {
    Page<Roadmap> getAllWithPageableAndFilter(RoadmapRequestFiltersDto filtersDto, Pageable pageable);
    Roadmap getById(String id);
    Roadmap getById(UUID id);
    Roadmap addRoadmap(RoadmapRequestDto roadmapDto);

    void deleteById(String id);

}

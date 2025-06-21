package com.roadmaps.Roadmaps.modules.roadmap.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RoadmapRequestFiltersDto {
    private List<String> statuses;
    private List<String> categoryIds;
    private String sortBy = "newest";
    private String orderBy = "descending";
}

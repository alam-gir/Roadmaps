package com.roadmaps.Roadmaps.cache;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;

public interface RoadmapCacheService {
    Roadmap getById(String string);

    void setById(String string, Roadmap roadmap);
}

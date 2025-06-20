package com.roadmaps.Roadmaps.cache;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;

import java.util.List;

public interface UpvoteCacheService {
    List<Upvote> getByRoadmapId(String roadmapId);

    void setByRoadmapId(String roadmapId, List<Upvote> upvotes);

    List<Upvote> getByCommentId(String string);

    void setByCommentId(String string, List<Upvote> upvotes);
}

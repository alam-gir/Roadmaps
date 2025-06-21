package com.roadmaps.Roadmaps.modules.roadmap.service;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;

import java.util.List;
import java.util.UUID;

public interface UpvoteService {
    List<Upvote> getUpvotesByRoadmapId(UUID roadmapId);

    List<Upvote> upvoteByUserEmailAndRoadmapId(String userEmail, String roadmapId);

    List<Upvote> getUpvotesByCommentId(UUID commentId);

    List<Upvote> upvoteByUserEmailAndCommentId(String userEmail, String roadmapId);
}

package com.roadmaps.Roadmaps.cache;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;

public interface CommentCacheService {
    Comment getById(String commentId);

    void setById(String commentId, Comment comment);
}

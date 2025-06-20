package com.roadmaps.Roadmaps.cache.impl;

import com.roadmaps.Roadmaps.cache.CommentCacheService;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentCacheServiceImpl implements CommentCacheService {
    private final RedisTemplate<String, Comment> redisTemplate;
}

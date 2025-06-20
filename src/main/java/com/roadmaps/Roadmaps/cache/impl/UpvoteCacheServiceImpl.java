package com.roadmaps.Roadmaps.cache.impl;

import com.roadmaps.Roadmaps.cache.UpvoteCacheService;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpvoteCacheServiceImpl implements UpvoteCacheService {
    private final RedisTemplate<String, List<Upvote>> redisTemplate;
}

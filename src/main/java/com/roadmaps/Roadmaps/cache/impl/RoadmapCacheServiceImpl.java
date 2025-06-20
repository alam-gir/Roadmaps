package com.roadmaps.Roadmaps.cache.impl;

import com.roadmaps.Roadmaps.cache.RoadmapCacheService;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoadmapCacheServiceImpl implements RoadmapCacheService {
    private final RedisTemplate<String, Roadmap> redisTemplate;

    @Override
    public Roadmap getById(String id) {
        try{
            String key = generateKey(id);
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Failed to get roadmap from cache by id : {}", id, e);
            return null;
        }
    }

    @Override
    public void setById(String id, Roadmap roadmap) {
        try{
            String key = generateKey(id);
            redisTemplate.opsForValue().set(key, roadmap);
        } catch (Exception e) {
            log.error("Failed to set roadmap to cache by id : {}", id, e);
        }
    }

    private String generateKey(String id) {
        String prefix = "roadmap:";
        return prefix + id;
    }
}

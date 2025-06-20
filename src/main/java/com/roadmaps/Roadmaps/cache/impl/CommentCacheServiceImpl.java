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

    @Override
    public Comment getById(String id) {
        try{
            String key = generateKey(id);
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Failed to get comment from cache by id : {}", id, e);
            return null;
        }
    }

    @Override
    public void setById(String id, Comment comment) {
        try{
            String key = generateKey(id);
            redisTemplate.opsForValue().set(key, comment);
        } catch (Exception e) {
            log.error("Failed to set comment to cache by id : {}", id, e);
        }
    }

    private String generateKey(String id) {
        String prefix = "comment:";
        return prefix + id;
    }
}

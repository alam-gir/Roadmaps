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

    @Override
    public List<Upvote> getByRoadmapId(String roadmapId) {
        try{
            String key = generateRoadmapUpvoteCacheKey(roadmapId);
            return redisTemplate.opsForValue().get(key);
        } catch(Exception e){
            log.error("Failed to get upvotes from cache for roadmap id : {}", roadmapId, e);
            return null;
        }
    }

    @Override
    public void setByRoadmapId(String roadmapId, List<Upvote> upvotes) {
        try{
            String key = generateRoadmapUpvoteCacheKey(roadmapId);
            redisTemplate.opsForValue().set(key,upvotes);
        } catch(Exception e){
            log.error("Failed to set upvotes to cache for roadmap id : {}", roadmapId, e);
        }
    }

    @Override
    public List<Upvote> getByCommentId(String commentId) {
        try{
            String key = generateCommentUpvoteCacheKey(commentId);
            return redisTemplate.opsForValue().get(key);
        } catch(Exception e){
            log.error("Failed to get upvotes from cache for comment id : {}", commentId, e);
            return null;
        }
    }

    @Override
    public void setByCommentId(String commentId, List<Upvote> upvotes) {
        try{
            String key = generateCommentUpvoteCacheKey(commentId);
            redisTemplate.opsForValue().set(key,upvotes);
        } catch(Exception e){
            log.error("Failed to set upvotes to cache for comment id : {}", commentId, e);
        }
    }

    private String generateRoadmapUpvoteCacheKey(String roadmapId){
        return  "roadmapId:" + roadmapId + ":upvotes";
    }
    private String generateCommentUpvoteCacheKey(String commentId){
        return  "commentId:" + commentId + ":upvotes";
    }
}

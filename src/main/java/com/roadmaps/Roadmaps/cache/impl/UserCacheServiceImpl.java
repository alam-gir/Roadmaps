package com.roadmaps.Roadmaps.cache.impl;

import com.roadmaps.Roadmaps.cache.UserCacheService;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCacheServiceImpl implements UserCacheService {
    private final RedisTemplate<String, User> userRedisTemplate;
    String userCachePrefix = "user:";
    Duration ttl = Duration.ofHours(1);


    @Override
    public User getUserByEmail(String email) {
        String normalizedEmail = email.toLowerCase();
        String key = userCachePrefix + normalizedEmail;
        return userRedisTemplate.opsForValue().get(key);
    }

    public void setUserByEmail(String email, User user) {
        String normalizedEmail = email.toLowerCase();
        String key = userCachePrefix + normalizedEmail;
        userRedisTemplate.opsForValue().set(key, user, ttl);
    }
}

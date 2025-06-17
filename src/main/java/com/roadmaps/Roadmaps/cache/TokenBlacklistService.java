package com.roadmaps.Roadmaps.cache;

import org.springframework.stereotype.Service;

@Service
public interface TokenBlacklistService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
}

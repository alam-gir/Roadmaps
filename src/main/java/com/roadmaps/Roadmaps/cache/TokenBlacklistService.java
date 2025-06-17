package com.roadmaps.Roadmaps.cache;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TokenBlacklistService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
    void blacklistAllTokensOfUser(UUID userId);
    void cleanupExpiredTokens();
}

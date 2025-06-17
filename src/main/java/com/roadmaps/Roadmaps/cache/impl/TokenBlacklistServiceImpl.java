package com.roadmaps.Roadmaps.cache.impl;

import com.roadmaps.Roadmaps.cache.TokenBlacklistService;

import java.util.UUID;

public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    @Override
    public void blacklistToken(String token) {

    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return false;
    }

    @Override
    public void blacklistAllTokensOfUser(UUID userId) {

    }

    @Override
    public void cleanupExpiredTokens() {

    }
}

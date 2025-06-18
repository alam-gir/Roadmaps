package com.roadmaps.Roadmaps.cache.impl;

import com.roadmaps.Roadmaps.cache.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtParser jwtParser;
    String tokenBlacklistKeyPrefix = "blacklist:";

    public TokenBlacklistServiceImpl(
            RedisTemplate<String, Object> redisTemplate,
            @Value("${jwt.secret}")  String secret
    ) {
        this.redisTemplate = redisTemplate;
        this.jwtParser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build();
    }

    @Override
    public void blacklistToken(String token) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            Date expiration = claims.getExpiration();

            if(expiration.after(new Date())){
                String tokenId = generateTokenId(token);
                long ttl = expiration.getTime() - System.currentTimeMillis();

                redisTemplate.opsForValue().set(
                        tokenBlacklistKeyPrefix + tokenId,
                        "blacklisted",
                        Duration.ofMillis(ttl)
                );

                log.debug("token blacklisted: {} with TTL {} ms", tokenId, ttl);
            }
        } catch (Exception ex) {
            log.error("Failed to blacklist token", ex);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            String tokenId = generateTokenId(token);
            return redisTemplate.hasKey(tokenBlacklistKeyPrefix + tokenId);
        } catch (Exception e) {
            log.error("Error to check blacklist token", e);
            return false;
        }
    }

    @Scheduled(fixedRate = 3600000) // clean up at every hour
    public void cleanupExpiredTokens() {
        try{
            String pattern = "blacklist:*";
            Set<String> keys = redisTemplate.keys(pattern);
            if(!keys.isEmpty()){
                long deleted = redisTemplate.delete(keys);
                if(deleted > 0){
                    log.debug("cleanup {} expires token", deleted);
                }
            }
        } catch (Exception ex) {
            log.error("Error when blacklist cleanup scheduled run!", ex);
        }
    }

    private String generateTokenId(String token) {
        try{
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            UUID userId = claims.get("userId", UUID.class);
            Date issuedAt = claims.get("issuedAt", Date.class);

            return Sha512DigestUtils.shaHex(
                    token.substring(0, Math.min(50, token.length())) +
                            ":user:" + userId + ":issued:" + issuedAt.getTime()
            );
        }  catch (Exception e){
            return Sha512DigestUtils.shaHex(token);
        }
    }
}

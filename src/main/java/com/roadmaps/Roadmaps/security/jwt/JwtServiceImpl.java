package com.roadmaps.Roadmaps.security.jwt;

import com.roadmaps.Roadmaps.cache.TokenBlacklistService;
import com.roadmaps.Roadmaps.common.exceptions.InvalidTokenException;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenExpiration:604800000}") // default 604800000 = 7days
    private long accessTokenExpiration;

    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public String generateAccessToken(UserPrinciple userPrinciple) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(accessTokenExpiration);

        return Jwts.builder()
                .subject(userPrinciple.getEmail())
                .claim("userId", userPrinciple.getId())
                .claim("tokenType", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(getSignKey(), Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        try{
            Claims claims = parseToken(token);
            return claims.getSubject();
        } catch (JwtException e){
            log.error("Invalid JWT token : {}", e.getMessage() );
            throw new InvalidTokenException();
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            if(tokenBlacklistService.isTokenBlacklisted(token)){
                log.warn("Token is blacklisted.");
                return false;
            }

            Claims claims = parseToken(token);
            String tokenType = claims.get("tokenType", String.class);

            return tokenType.equals("access") && !isTokenExpired(token);
        } catch (ExpiredJwtException ex){
            log.debug("Access token expired. {}", ex.getMessage());
            return false;
        } catch (JwtException ex) {
            log.error("Token validation failed. {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try{
            if(tokenBlacklistService.isTokenBlacklisted(token))
                return false;

            return isTokenExpired(token);
        } catch (Exception e) {
            log.debug("Token validation failed. {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try{
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername())
                    && isTokenValid(token);
        } catch (Exception e) {
            log.debug("Token validation failed. {}", e.getMessage());
            return false;
        }
    }

    private Claims parseToken(String token) {
        try {
            JwtParser jwtParser = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build();

            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException ex){
            log.debug("Token expired. {}", ex.getMessage());
            throw new InvalidTokenException();
        } catch (UnsupportedJwtException ex) {
            log.debug("Token unsupported. {}", ex.getMessage());
            throw new InvalidTokenException();
        } catch (MalformedJwtException ex) {
            log.debug("Token malformed. {}", ex.getMessage());
            throw new InvalidTokenException();
        } catch (SignatureException ex) {
            log.debug("Token signature exception. {}", ex.getMessage());
            throw new InvalidTokenException();
        } catch (IllegalArgumentException ex) {
            log.debug("Token claims empty. {}", ex.getMessage());
            throw new InvalidTokenException();
        }
    }

    private boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(Date.from(Instant.now()));
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}

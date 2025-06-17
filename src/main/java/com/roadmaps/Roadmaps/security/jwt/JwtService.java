package com.roadmaps.Roadmaps.security.jwt;

import com.roadmaps.Roadmaps.security.UserPrinciple;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserPrinciple userPrinciple);
    String extractUsername(String token);
    boolean validateToken(String token);
    boolean isTokenValid(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}

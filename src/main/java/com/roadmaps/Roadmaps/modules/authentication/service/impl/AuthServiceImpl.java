package com.roadmaps.Roadmaps.modules.authentication.service.impl;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.common.utils.CookieUtils;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.LoginRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.SignupRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.service.AuthService;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import com.roadmaps.Roadmaps.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @Value("${jwt.accessTokenExpiration:604800000}")
    long accessTokenExpirationInMillis;

    @Override
    public ApiResponse<?> signup(SignupRequestDto signupDto) {
        userService.addUser(signupDto);
        return ApiResponse.success(null, "Account created.");
    }

    @Override
    public ApiResponse<?> login(HttpServletResponse response, LoginRequestDto loginDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        UserPrinciple userPrinciple = (UserPrinciple) auth.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userPrinciple);

        CookieUtils.addCookie(
                response,
                "accessToken",
                accessToken,
                (int) (accessTokenExpirationInMillis / 1000)
        );

        return ApiResponse.success(null, "Login successful.");
    }
}

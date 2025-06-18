package com.roadmaps.Roadmaps.modules.authentication.service.impl;

import com.roadmaps.Roadmaps.common.exceptions.InvalidEmailPasswordException;
import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.common.utils.CookieUtils;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.LoginRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.SignupRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.service.AuthService;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.events.UserRegistrationEvent;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import com.roadmaps.Roadmaps.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${jwt.accessTokenExpiration:604800000}")
    long accessTokenExpirationInMillis;

    @Value("${frontendBaseUrl}")
    String frontendBaseUrl;

    @Override
    public ApiResponse<?> signup(SignupRequestDto signupDto) {
        User user = userService.addUser(signupDto);

        // publish event to sent verification email to user
        eventPublisher.publishEvent(new UserRegistrationEvent(this, user, frontendBaseUrl));

        return ApiResponse.success(null, "Account created.");
    }

    @Override
    public ApiResponse<?> login(HttpServletResponse response, LoginRequestDto loginDto) {
        try {
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
        } catch (AuthenticationException ex) {
            log.warn("Authenticateion Exception when login : {}", ex.getMessage());
            throw new InvalidEmailPasswordException();
        }
    }
}

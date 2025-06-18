package com.roadmaps.Roadmaps.modules.authentication.service.impl;

import com.roadmaps.Roadmaps.cache.TokenBlacklistService;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.InvalidEmailPasswordException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.common.utils.CookieUtils;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.LoginRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.SignupRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.service.AuthService;
import com.roadmaps.Roadmaps.modules.user.enities.EmailVerificationToken;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.events.UserRegistrationEvent;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import com.roadmaps.Roadmaps.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenBlacklistService tokenBlacklistService;

    @Value("${jwt.accessTokenExpiration:604800000}")
    long accessTokenExpirationInMillis;

    @Value("${frontendBaseUrl}")
    String frontendBaseUrl;

    @Override
    @Transactional
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

    @Override
    public ApiResponse<?> logout(HttpServletResponse response, HttpServletRequest request) {
        try{
            CookieUtils.getCookie(request, "accessToken")
                    .ifPresentOrElse(
                            tokenBlacklistService::blacklistToken,
                            () -> {
                                throw new NotFoundException("Token not found");
                            }
                    );

            CookieUtils.removeCookie(request,response,"accessToken");

            return ApiResponse.success(null, "Logout successful.");
        } catch (Exception ex) {
            log.error("Logout failed when login : {}", ex.getMessage());
            throw new ApiException("Something went wrong when Logout.");
        }
    }

    @Override
    public String generateEmailVerificationToken(User user) {
        try{
            EmailVerificationToken token = new EmailVerificationToken();
            token.setToken(UUID.randomUUID().toString());
            token.setExpiredAt(LocalDateTime.now().plusMinutes(5));

            user.setVerificationToken(token);
            userService.update(user);

            return token.getToken();
        } catch (Exception ex) {
            log.error("Error while generating emailVerificationToken", ex);
        }
        return null;
    }

    @Override
    @Transactional
    public void verifyEmail(String userEmail, String token) {
        try{
            User user = userService.getUserByEmail(userEmail);

            checkUserAlreadyVerified(user);

            validateToken(user.getVerificationToken(), token);

            markUserVerified(user);

        } catch (NotFoundException ex) {
            log.warn(ex.getMessage());
            throw new ApiException("Invalid link!");
        } catch (ApiException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error while verifying email", ex);
            throw new ApiException("Invalid link!");
        }
    }

    @Override
    public void getVerificationLink(UserPrinciple userPrinciple) {
        User user = userService.getUserByEmail(userPrinciple.getEmail());
        checkUserAlreadyVerified(user);
        eventPublisher.publishEvent(new UserRegistrationEvent(this, user, frontendBaseUrl));
    }

    private void checkUserAlreadyVerified(User user) {
        if(user.isEmailVerified()) {
            throw new ApiException("Email already verified");
        }
    }

    private void markUserVerified(User user) {
        user.setVerificationToken(null);
        user.setEmailVerified(true);

        userService.update(user);
    }

    private void validateToken(EmailVerificationToken verificationToken, String token) {
        if(
                verificationToken == null
                        || verificationToken.getExpiredAt().isBefore(LocalDateTime.now())
                        || !verificationToken.getToken().equals(token)
        ) {
            throw new ApiException("Invalid link!. Get new link and try again.");
        }
    }
}

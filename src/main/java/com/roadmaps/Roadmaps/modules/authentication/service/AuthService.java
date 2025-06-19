package com.roadmaps.Roadmaps.modules.authentication.service;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.LoginRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.SignupRequestDto;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    ApiResponse<?> signup(SignupRequestDto signupDto);
    ApiResponse<?> login(HttpServletResponse response, LoginRequestDto loginDto);
    ApiResponse<?> logout(HttpServletResponse response, HttpServletRequest request);
    void verifyEmail(String email, String token);
    void getVerificationLink(UserPrinciple userPrinciple);
}

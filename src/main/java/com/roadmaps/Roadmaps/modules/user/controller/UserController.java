package com.roadmaps.Roadmaps.modules.user.controller;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.mapper.UserMapper;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController{
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getLoggedInUser(Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        User user = userService.getUserByEmail(userPrinciple.getEmail());

        ApiResponse<?> apiResponse = ApiResponse.success(
                userMapper.toLoggedInUserResponseDto(user),
                null
        );

        return ResponseEntity.ok(apiResponse);
    }
}

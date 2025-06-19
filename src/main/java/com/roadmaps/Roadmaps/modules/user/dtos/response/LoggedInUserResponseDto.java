package com.roadmaps.Roadmaps.modules.user.dtos.response;

public record LoggedInUserResponseDto (
        String id,
        String name,
        String email,
        String role,
        boolean isEmailVerified
) {}

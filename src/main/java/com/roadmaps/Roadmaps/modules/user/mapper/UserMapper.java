package com.roadmaps.Roadmaps.modules.user.mapper;

import com.roadmaps.Roadmaps.modules.user.dtos.UserRequestDto;
import com.roadmaps.Roadmaps.modules.user.dtos.response.LoggedInUserResponseDto;
import com.roadmaps.Roadmaps.modules.user.enities.EmailVerificationToken;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toEntityWithEncryptedPassword(UserRequestDto userDto) {
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(encryptedPassword)
                .build();
    }

    public Object toLoggedInUserResponseDto(User user) {
        return new LoggedInUserResponseDto(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.isEmailVerified()
        );
    }

    public EmailVerificationToken getNewEmailVerificationToken() {
        return EmailVerificationToken.builder()
                    .token(UUID.randomUUID().toString())
                    .expiredAt(LocalDateTime.now().plusMinutes(5))
                    .build();
    }
}

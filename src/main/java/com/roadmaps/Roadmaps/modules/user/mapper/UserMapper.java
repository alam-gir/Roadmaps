package com.roadmaps.Roadmaps.modules.user.mapper;

import com.roadmaps.Roadmaps.modules.user.dtos.UserRequestDto;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
}

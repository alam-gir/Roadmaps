package com.roadmaps.Roadmaps.modules.user.mapper;

import com.roadmaps.Roadmaps.modules.user.dtos.UserRequestDto;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntityWithEncryptedPassword(UserRequestDto userDto) {
        String encryptedPassword = userDto.getPassword();
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(encryptedPassword)
                .build();
    }
}

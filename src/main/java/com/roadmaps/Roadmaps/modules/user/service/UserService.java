package com.roadmaps.Roadmaps.modules.user.service;

import com.roadmaps.Roadmaps.modules.user.dtos.UserRequestDto;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getUserByEmail(String email);
    User addUser(UserRequestDto userDto);
}

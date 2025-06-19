package com.roadmaps.Roadmaps.modules.user.service;
import com.roadmaps.Roadmaps.modules.user.enities.User;

public interface UserService {
    User getUserByEmail(String email);
    User save(User user);
}

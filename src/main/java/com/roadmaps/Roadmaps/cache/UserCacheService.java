package com.roadmaps.Roadmaps.cache;

import com.roadmaps.Roadmaps.modules.user.enities.User;

public interface UserCacheService {
    User getUserByEmail(String email);
    void setUserByEmail(String email, User user);
    void removeUserByEmail(String email);
}

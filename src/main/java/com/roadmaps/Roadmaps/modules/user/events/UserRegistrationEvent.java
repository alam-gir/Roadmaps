package com.roadmaps.Roadmaps.modules.user.events;

import com.roadmaps.Roadmaps.modules.user.enities.User;
import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {
    String frontendBaseUrl;
    User user;

    public UserRegistrationEvent(Object source, User user, String frontendBaseUrl) {
        super(source);
        this.user = user;
        this.frontendBaseUrl = frontendBaseUrl;
    }
}

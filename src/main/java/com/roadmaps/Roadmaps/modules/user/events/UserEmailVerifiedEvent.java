package com.roadmaps.Roadmaps.modules.user.events;

import com.roadmaps.Roadmaps.modules.user.enities.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEmailVerifiedEvent extends ApplicationEvent {
    private String frontendBaseUrl;
    private User user;

    public UserEmailVerifiedEvent(Object source, User user,  String frontendBaseUrl) {
        super(source);
        this.user = user;
        this.frontendBaseUrl = frontendBaseUrl;
    }
}

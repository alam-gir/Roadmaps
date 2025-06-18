package com.roadmaps.Roadmaps.modules.user.events.listener;

import com.roadmaps.Roadmaps.modules.user.events.UserRegistrationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener {

    @EventListener
    @Async("user-registration-event-lister")
    public void handleUserRegistrationEvent(UserRegistrationEvent userRegistrationEvent) {

    }
}

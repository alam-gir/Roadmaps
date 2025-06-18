package com.roadmaps.Roadmaps.modules.user.events.listener;

import com.roadmaps.Roadmaps.modules.email.service.EmailService;
import com.roadmaps.Roadmaps.modules.user.events.UserEmailVerifiedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEmailVerifiedEventListener {
    private final EmailService emailService;

    @EventListener
    @Async("user-registration-event-lister")
    public void handleUserEmailVerifiedEvent(UserEmailVerifiedEvent event) {
        emailService.sendWelcomeEmailForEmailVerificationAsync(
                event.getUser().getEmail(),
                event.getUser().getName(),
                event.getFrontendBaseUrl()
        );
    }

}

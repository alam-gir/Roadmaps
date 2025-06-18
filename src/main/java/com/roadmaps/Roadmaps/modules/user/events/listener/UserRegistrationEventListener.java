package com.roadmaps.Roadmaps.modules.user.events.listener;

import com.roadmaps.Roadmaps.modules.authentication.service.AuthService;
import com.roadmaps.Roadmaps.modules.email.service.EmailService;
import com.roadmaps.Roadmaps.modules.user.events.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegistrationEventListener {
    private final AuthService authService;
    private final EmailService emailService;

    @EventListener
    @Async("user-registration-event-lister")
    public void handleUserRegistrationEvent(UserRegistrationEvent userRegistrationEvent) {
        String userEmail = userRegistrationEvent.getUser().getEmail();
        String frontendBaseUrl = userRegistrationEvent.getFrontendBaseUrl();
        String verificationToken = authService.generateEmailVerificationToken(userRegistrationEvent.getUser());
        if(verificationToken != null){
            emailService.sendVerificationEmailAsync(userEmail, verificationToken, frontendBaseUrl );
        } else {
            log.error("Email verification token could not be generated for user {}", userEmail);
        }
    }
}

package com.roadmaps.Roadmaps.modules.user.events.listener;

import com.roadmaps.Roadmaps.modules.email.service.EmailService;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.events.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegistrationEventListener {
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("user-registration-event-lister")
    public void handleUserRegistrationEvent(UserRegistrationEvent userRegistrationEvent) {
        User user = userRegistrationEvent.getUser();
        if(user == null) return;

        String userEmail = user.getEmail();
        String frontendBaseUrl = userRegistrationEvent.getFrontendBaseUrl();
        String verificationToken = user.getVerificationToken().getToken();
        if(verificationToken != null){
            emailService.sendVerificationEmailAsync(userEmail, verificationToken, frontendBaseUrl );
        } else {
            log.error("Email verification token could not be generated for user {}", userEmail);
        }
    }
}

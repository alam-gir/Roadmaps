package com.roadmaps.Roadmaps.modules.email.service;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<Boolean> sendVerificationEmailAsync(String toEmail, String verificationToken, String frontendBaseUrl);
    CompletableFuture<Boolean> sendWelcomeEmailForEmailVerificationAsync(String toEmail, String userName, String frontendBaseUrl);
}

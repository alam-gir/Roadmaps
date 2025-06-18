package com.roadmaps.Roadmaps.modules.email.service.impl;

import com.roadmaps.Roadmaps.modules.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    @Retryable(
            retryFor = {Exception.class},
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public CompletableFuture<Boolean> sendVerificationEmailAsync(String toEmail, String verificationToken, String frontendBaseUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                sendVerificationEmail(toEmail, verificationToken, frontendBaseUrl);
                return true;
            } catch (Exception ex) {
                log.error("Failed to send verification email : {}",toEmail, ex);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> sendWelcomeEmailForEmailVerificationAsync(String toEmail, String userName, String frontendBaseUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                sendWelcomeEmailForEmailVerification(toEmail, userName, frontendBaseUrl);
                return true;
            } catch (Exception ex) {
                log.error("Failed to send welcome email for verified email to : {}",userName, ex);
                return false;
            }
        });
    }

    private void sendVerificationEmail(String toEmail, String verificationToken, String frontendBaseUrl) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String verificationUrl = frontendBaseUrl + "/verify-email?token=" + verificationToken;
            String subject = "Verify Email at Roadmaps";
            String textBody = "Hello,\n\nPlease verify your email by clicking the link below:\n" + verificationUrl + "\n\nIf you didn't create an account, you can ignore this message.";
            String htmlContent = """
                    <html>
                    <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                        <div style="max-width: 600px; margin: auto; background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                            <h2 style="color: #333;">Confirm Your Email Address</h2>
                            <p style="font-size: 16px; color: #555;">
                                Thank you for registering! Please verify your email address by clicking the button below:
                            </p>
                            <div style="text-align: center; margin: 30px 0;">
                                <a href="%s" style="background-color: #4CAF50; color: white; text-decoration: none; padding: 12px 24px; border-radius: 5px; font-size: 16px;">
                                    Verify Email
                                </a>
                            </div>
                            <p style="font-size: 14px; color: #777;">
                                Or copy and paste this link into your browser:
                                <br/>
                                <a href="%s" style="color: #4CAF50;">%s</a>
                            </p>
                            <p style="font-size: 14px; color: #aaa;">If you didn’t create an account, you can ignore this message.</p>
                        </div>
                    </body>
                    </html>
                    """.formatted(verificationUrl, verificationUrl, verificationUrl);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(textBody, htmlContent);
            mailSender.send(message);
        } catch(Exception ex) {
            log.error("Failed to send verification email : {}",toEmail, ex);
        }
    }


    private void sendWelcomeEmailForEmailVerification(String toEmail, String userName, String frontendBaseUrl) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String subject = "Welcome to Roadmaps";
            String textBody = """
                            Hello %s,

                            Welcome to Roadmaps! We're excited to have you on board.
                    
                            You can now start exploring roadmaps and interact with all the roadmaps.
                    
                            If you have any questions or need help, feel free to reach out.
                    
                            Happy learning!
                            – The Roadmaps Team
                    """.formatted(userName);;
            String htmlContent = """
                            <html>
                            <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                                <div style="max-width: 600px; margin: auto; background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                    <h2 style="color: #333;">Welcome to Roadmaps, %s!</h2>
                                    <p style="font-size: 16px; color: #555;">
                                        We're thrilled to have you join our community. You can now start explore roadmaps, and interact with all the roadmaps.
                                    </p>
                                    <div style="text-align: center; margin: 30px 0;">
                                        <a href="%s" style="background-color: #4CAF50; color: white; text-decoration: none; padding: 12px 24px; border-radius: 5px; font-size: 16px;">
                                            Explore Roadmaps
                                        </a>
                                    </div>
                                    <p style="font-size: 14px; color: #777;">
                                        If you have any questions or need help, feel free to contact our support team.
                                    </p>
                                    <p style="font-size: 14px; color: #aaa;">Let’s build your learning path together!</p>
                                </div>
                            </body>
                            </html>
                            """.formatted(userName, frontendBaseUrl);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(textBody, htmlContent);
            mailSender.send(message);
        } catch(Exception ex) {
            log.error("Failed to send verification email : {}",toEmail, ex);
        }
    }
}

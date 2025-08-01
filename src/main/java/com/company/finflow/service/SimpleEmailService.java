package com.company.finflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@Profile("test-email")
public class SimpleEmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        try {
            String verificationLink = "https://192.168.1.157:8443/verify?token=" + verificationToken;
            
            // DEBUG: Log del link
            System.out.println("🔗 HTTPS VERIFICATION LINK: " + verificationLink);
            System.out.println("🎫 TOKEN: " + verificationToken);
            
            // Leggi template HTML
            ClassPathResource resource = new ClassPathResource("templates/email-verification-template.html");
            String htmlTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            
            // Sostituisci placeholder con link reale
            String htmlContent = htmlTemplate.replace("{{VERIFICATION_LINK}}", verificationLink);
            
            // Crea email HTML + Plain Text (Multipart)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject("Verify Your FinFlow Account - Action Required");
            
            // Testo plain per anti-spam
            String plainText = "Welcome to FinFlow!\n\n" +
                             "Please verify your account by clicking this link:\n" +
                             verificationLink + "\n\n" +
                             "This link expires in 24 hours.\n\n" +
                             "Best regards,\n" +
                             "FinFlow Support Team";
            
            helper.setText(plainText, htmlContent); // plain text + HTML
            helper.setFrom("nicholassimone010@gmail.com", "FinFlow Support");
            
            // Anti-SPAM headers
            helper.getMimeMessage().setHeader("Reply-To", "nicholassimone010@gmail.com");
            helper.getMimeMessage().setHeader("Return-Path", "nicholassimone010@gmail.com");
            helper.getMimeMessage().setHeader("X-Priority", "3");
            helper.getMimeMessage().setHeader("X-Mailer", "FinFlow Application");
            helper.getMimeMessage().setHeader("List-Unsubscribe", "<mailto:nicholassimone010@gmail.com>");
            
            mailSender.send(message);
            
            System.out.println("✅ EMAIL HTML PROFESSIONALE SENT TO: " + toEmail);
            
        } catch (Exception e) {
            System.err.println("❌ EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

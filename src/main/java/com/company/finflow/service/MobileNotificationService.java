package com.company.finflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("mobile")
public class MobileNotificationService implements IEmailService {

    @Value("${app.base-url:http://192.168.1.157:8010}")
    private String baseUrl;
    
    @Autowired
    private WebNotificationService webNotificationService;
    
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        String verificationLink = baseUrl + "/verify?token=" + verificationToken;
        
        try {
            // Invio EMAIL REALE
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("🎉 Welcome to FinFlow - Verify Your Account");
            message.setText("Welcome to FinFlow!\n\n" +
                    "Thank you for registering. Please click the link below to verify your account:\n\n" +
                    verificationLink + "\n\n" +
                    "This link will expire in 24 hours.\n\n" +
                    "Best regards,\n" +
                    "The FinFlow Team 💰");
            
            mailSender.send(message);
            
            // Log per debugging
            System.out.println("\n" + "=".repeat(80));
            System.out.println("📧 EMAIL REALE INVIATA: " + toEmail);
            System.out.println("🔗 Link di verifica: " + verificationLink);
            System.out.println("=".repeat(80));
            
            // BONUS: Notifica web di conferma invio email
            String username = toEmail.split("@")[0];
            String message_web = "✅ Verification email sent! Please check your inbox and spam folder.";
            webNotificationService.sendNotification(username, message_web, "success");
            
        } catch (Exception e) {
            System.err.println("❌ Errore invio email a " + toEmail + ": " + e.getMessage());
            
            // Se email fallisce, almeno mostra notifica web
            String username = toEmail.split("@")[0];
            String message_web = "⚠️ Email delivery may be delayed. Please check your email in a few minutes.";
            webNotificationService.sendNotification(username, message_web, "warning");
        }
    }
}

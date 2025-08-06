package com.company.finflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class CmdNotificationService implements IEmailService {

    @Value("${app.base.url:http://localhost:8010}")
    private String baseUrl;
    
    @Autowired
    private WindowsNotificationService windowsNotificationService;

    @Override
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        String verificationLink = baseUrl + "/verify?token=" + verificationToken;
        
        // Mostra nella console per debugging
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📧 NOTIFICA VERIFICA ACCOUNT: " + toEmail);
        System.out.println("=".repeat(80));
        System.out.println("🎯 Oggetto: 🎉 Benvenuto su FinFlow - Verifica il tuo account");
        System.out.println("🔗 Link di verifica: " + verificationLink);
        System.out.println("=".repeat(80));
        
        // Mostra ENTRAMBE le notifiche: Windows + CMD
        try {
            // Notifica Windows (toast)
            windowsNotificationService.showVerificationNotification(toEmail);
            System.out.println("✅ NOTIFICA WINDOWS INVIATA CON SUCCESSO!");
            
            // Finestra CMD (visibile)
            openCmdWithVerificationMessage(toEmail, verificationLink);
            System.out.println("✅ FINESTRA CMD APERTA CON SUCCESSO!");
        } catch (Exception e) {
            System.out.println("❌ ERRORE NOTIFICHE: " + e.getMessage());
            System.out.println("💡 Usa il link dalla console per verificare l'account manualmente");
        }
        
        System.out.println("=".repeat(80) + "\n");
    }
    
    private void openCmdWithVerificationMessage(String toEmail, String verificationLink) throws Exception {
        // Comando per finestra CMD ben visibile
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", 
            "start", "\"FinFlow - Verifica Account\"", "/max", "cmd", "/c", 
            "title FinFlow - Verifica Account & " +
            "color 0A & " +
            "mode con: cols=90 lines=30 & " +
            "echo. & " +
            "echo =============================================== & " +
            "echo    🎉 BENVENUTO SU FINFLOW! & " +
            "echo =============================================== & " +
            "echo. & " +
            "echo 📧 Account: " + toEmail + " & " +
            "echo. & " +
            "echo ✅ Per completare la registrazione, & " +
            "echo    copia e incolla questo link nel browser: & " +
            "echo. & " +
            "echo 🔗 " + verificationLink + " & " +
            "echo. & " +
            "echo =============================================== & " +
            "echo    💡 Mantieni questa finestra aperta & " +
            "echo       finche non hai verificato l'account & " +
            "echo =============================================== & " +
            "echo. & " +
            "echo 🔔 IMPORTANTE: Clicca su questo link! & " +
            "echo. & " +
            "pause"
        );
        
        pb.start();
    }
}

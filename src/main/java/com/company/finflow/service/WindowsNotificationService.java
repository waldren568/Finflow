package com.company.finflow.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.text.DecimalFormat;

@Service
public class WindowsNotificationService {
    
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    
    public void showGoalCompletionNotification(String goalName, Double currentAmount, Double targetAmount) {
        try {
            String title = "🎉 Obiettivo Completato!";
            String message = String.format(
                "Hai raggiunto l'obiettivo '%s'!\n💰 Importo: €%s di €%s",
                goalName,
                currencyFormat.format(currentAmount),
                currencyFormat.format(targetAmount)
            );
            
            showWindowsToast(title, message, "success");
            System.out.println("✅ NOTIFICA WINDOWS INVIATA: " + goalName);
        } catch (Exception e) {
            System.out.println("❌ ERRORE NOTIFICA WINDOWS: " + e.getMessage());
        }
    }
    
    public void showGoalProgressNotification(String goalName, Double currentAmount, Double targetAmount, Double remaining) {
        try {
            String title = "🔥 Quasi ci siamo!";
            String message = String.format(
                "Obiettivo '%s' al %.1f%%!\n💪 Mancano solo €%s",
                goalName,
                (currentAmount / targetAmount) * 100,
                currencyFormat.format(remaining)
            );
            
            showWindowsToast(title, message, "warning");
            System.out.println("🔥 NOTIFICA PROGRESSO WINDOWS INVIATA: " + goalName);
        } catch (Exception e) {
            System.out.println("❌ ERRORE NOTIFICA PROGRESSO: " + e.getMessage());
        }
    }
    
    public void showVerificationNotification(String email) {
        try {
            String title = "📧 Verifica Account FinFlow";
            String message = String.format(
                "Ciao! Controlla la console per il link di verifica per %s",
                email
            );
            
            showWindowsToast(title, message, "info");
            System.out.println("📧 NOTIFICA VERIFICA WINDOWS INVIATA: " + email);
        } catch (Exception e) {
            System.out.println("❌ ERRORE NOTIFICA VERIFICA: " + e.getMessage());
        }
    }
    
    private void showWindowsToast(String title, String message, String type) throws IOException {
        // Usa MessageBox che è sempre visibile e garantito
        try {
            String icon = getMessageBoxIcon(type);
            String script = String.format(
                "Add-Type -AssemblyName System.Windows.Forms; " +
                "[System.Windows.Forms.MessageBox]::Show('%s', '%s', 'OK', '%s')",
                escapeForPowerShell(message),
                escapeForPowerShell(title),
                icon
            );
            
            ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-WindowStyle", "Hidden", "-Command", script);
            pb.start();
            
            // Suono di sistema
            playSystemSound(getAudioForType(type));
            
        } catch (Exception e) {
            // Fallback ancora più semplice - CMD popup
            try {
                String cmd = String.format(
                    "start \"\" cmd /c \"title %s & echo. & echo %s & echo. & echo %s & echo. & pause\"",
                    escapeForCmd(title),
                    escapeForCmd(title),
                    escapeForCmd(message)
                );
                
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", cmd);
                pb.start();
                
                // Suono di sistema
                playSystemSound(getAudioForType(type));
                
            } catch (Exception e2) {
                // Ultimo fallback - solo suono e log
                playSystemSound(getAudioForType(type));
                System.out.println("🔔 NOTIFICA: " + title + " - " + message);
            }
        }
    }
    
    private String escapeForCmd(String text) {
        return text.replace("&", "^&").replace("|", "^|").replace("<", "^<").replace(">", "^>");
    }
    
    private String getMessageBoxIcon(String type) {
        switch (type) {
            case "success": return "Information";
            case "warning": return "Warning"; 
            case "error": return "Error";
            default: return "Information";
        }
    }
    
    private String getAudioForType(String type) {
        switch (type) {
            case "success": return "SystemExclamation";
            case "warning": return "SystemExclamation";
            case "error": return "SystemHand";
            default: return "SystemAsterisk";
        }
    }
    
    private void playSystemSound(String soundType) {
        try {
            // Prova prima con PowerShell
            ProcessBuilder soundPb = new ProcessBuilder(
                "powershell.exe", "-Command", 
                String.format("[System.Media.SystemSounds]::%s.Play()", soundType)
            );
            soundPb.start();
        } catch (Exception e) {
            // Fallback con rundll32
            try {
                String beepType = "0x40"; // Default beep
                switch (soundType) {
                    case "SystemExclamation": beepType = "0x30"; break;
                    case "SystemHand": beepType = "0x10"; break;
                    case "SystemAsterisk": beepType = "0x40"; break;
                }
                ProcessBuilder fallbackPb = new ProcessBuilder("rundll32", "user32.dll,MessageBeep", beepType);
                fallbackPb.start();
            } catch (Exception e2) {
                // Fallback finale - beep console
                try {
                    ProcessBuilder consolePb = new ProcessBuilder("cmd", "/c", "echo \\a");
                    consolePb.start();
                } catch (Exception ignored) {
                    System.out.println("🔊 BEEP! (audio fallback)");
                }
            }
        }
    }
    
    private String escapeForPowerShell(String text) {
        return text.replace("'", "''").replace("\"", "`\"").replace("\n", " ");
    }
}

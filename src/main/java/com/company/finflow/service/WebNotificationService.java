package com.company.finflow.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Servizio per notifiche web che funziona su qualsiasi dispositivo
 * Le notifiche vengono mostrate direttamente nel browser
 */
@Service
public class WebNotificationService {
    
    // Map per tenere traccia delle notifiche per utente
    private final ConcurrentHashMap<String, List<WebNotification>> userNotifications = new ConcurrentHashMap<>();
    
    public static class WebNotification {
        private String message;
        private String type; // success, info, warning, error
        private long timestamp;
        private boolean read;
        
        public WebNotification(String message, String type) {
            this.message = message;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
            this.read = false;
        }
        
        // Getters e setters
        public String getMessage() { return message; }
        public String getType() { return type; }
        public long getTimestamp() { return timestamp; }
        public boolean isRead() { return read; }
        public void setRead(boolean read) { this.read = read; }
    }
    
    /**
     * Invia una notifica di successo per registrazione
     */
    public void sendRegistrationNotification(String username) {
        String message = "✅ Account created successfully! Check your email to verify your account.";
        addNotification(username, message, "success");
        System.out.println("📱 Web Notification: " + message + " (User: " + username + ")");
    }
    
    /**
     * Invia una notifica per completamento goal
     */
    public void sendGoalCompletionNotification(String username, String goalName) {
        String message = "🎉 Congratulations! You completed your goal: " + goalName;
        addNotification(username, message, "success");
        System.out.println("📱 Web Notification: " + message + " (User: " + username + ")");
    }
    
    /**
     * Invia una notifica generica
     */
    public void sendNotification(String username, String message, String type) {
        addNotification(username, message, type);
        System.out.println("📱 Web Notification: " + message + " (User: " + username + ", Type: " + type + ")");
    }
    
    private void addNotification(String username, String message, String type) {
        userNotifications.computeIfAbsent(username, k -> new ArrayList<>())
                        .add(new WebNotification(message, type));
    }
    
    /**
     * Ottiene tutte le notifiche non lette per un utente
     */
    public List<WebNotification> getUnreadNotifications(String username) {
        return userNotifications.getOrDefault(username, new ArrayList<>())
                .stream()
                .filter(n -> !n.isRead())
                .toList();
    }
    
    /**
     * Marca tutte le notifiche come lette
     */
    public void markAllAsRead(String username) {
        userNotifications.getOrDefault(username, new ArrayList<>())
                .forEach(n -> n.setRead(true));
    }
}

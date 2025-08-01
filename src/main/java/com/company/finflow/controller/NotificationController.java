package com.company.finflow.controller;

import com.company.finflow.service.WebNotificationService;
import com.company.finflow.service.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private WebNotificationService webNotificationService;
    
    @Autowired
    private CurrentUserUtil currentUserUtil;
    
    /**
     * Ottiene tutte le notifiche non lette per l'utente corrente
     */
    @GetMapping("/unread")
    public ResponseEntity<List<WebNotificationService.WebNotification>> getUnreadNotifications() {
        try {
            String username = currentUserUtil.getCurrentUser().getUsername();
            List<WebNotificationService.WebNotification> notifications = 
                webNotificationService.getUnreadNotifications(username);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // Restituisce lista vuota se non autenticato
        }
    }
    
    /**
     * Marca tutte le notifiche come lette
     */
    @PostMapping("/mark-read")
    public ResponseEntity<String> markAllAsRead() {
        try {
            String username = currentUserUtil.getCurrentUser().getUsername();
            webNotificationService.markAllAsRead(username);
            return ResponseEntity.ok("All notifications marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error marking notifications as read");
        }
    }
}

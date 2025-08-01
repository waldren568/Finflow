package com.company.finflow.service;

import com.company.finflow.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoalNotificationService {
    
    @Autowired
    private WindowsNotificationService windowsNotificationService;
    
    @Autowired
    private WebNotificationService webNotificationService;
    
    @Value("${spring.profiles.active}")
    private String activeProfile;
    
    public void checkAndNotifyGoalCompletion(Goal goal) {
        double progress = goal.getProgressPercentage();
        System.out.println("🔍 CONTROLLO OBIETTIVO: " + goal.getName() + " - Progresso: " + progress + "%");
        
        if (isGoalCompleted(goal)) {
            if ("mobile".equals(activeProfile)) {
                // Notifica web per mobile
                webNotificationService.sendGoalCompletionNotification(
                    goal.getUser().getUsername(), 
                    goal.getName()
                );
            } else {
                // Notifica Windows per desktop
                windowsNotificationService.showGoalCompletionNotification(
                    goal.getName(), 
                    goal.getCurrentAmount(), 
                    goal.getTargetAmount()
                );
            }
            System.out.println("🎉 OBIETTIVO COMPLETATO: " + goal.getName() + " - NOTIFICA INVIATA!");
        } else if (isGoalNearCompletion(goal)) {
            double remaining = goal.getTargetAmount() - goal.getCurrentAmount();
            if ("mobile".equals(activeProfile)) {
                // Notifica web per mobile
                webNotificationService.sendNotification(
                    goal.getUser().getUsername(), 
                    "🔥 You're almost there! Only " + String.format("%.2f", remaining) + "€ left to reach '" + goal.getName() + "'", 
                    "info"
                );
            } else {
                // Notifica Windows per desktop
                windowsNotificationService.showGoalProgressNotification(
                    goal.getName(),
                    goal.getCurrentAmount(),
                    goal.getTargetAmount(),
                    remaining
                );
            }
            System.out.println("🔥 OBIETTIVO QUASI RAGGIUNTO: " + goal.getName() + " - NOTIFICA INVIATA!");
        } else {
            System.out.println("📊 Obiettivo '" + goal.getName() + "' al " + String.format("%.1f", progress) + "% - Nessuna notifica necessaria");
        }
    }
    
    private boolean isGoalCompleted(Goal goal) {
        return goal.getCurrentAmount() >= goal.getTargetAmount();
    }
    
    private boolean isGoalNearCompletion(Goal goal) {
        double progress = goal.getProgressPercentage();
        return progress >= 90.0 && progress < 100.0;
    }
}

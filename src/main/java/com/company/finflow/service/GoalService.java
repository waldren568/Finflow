package com.company.finflow.service;

import com.company.finflow.model.Goal;
import com.company.finflow.repository.GoalRepository;
import com.company.finflow.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import com.company.finflow.service.CurrentUserUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {
    
    @Autowired
    private GoalNotificationService goalNotificationService;
    public List<Goal> findGoalsByUser(AppUser user) {
        return goalRepository.findByUser(user);
    }
    @Autowired
    private CurrentUserUtil currentUserUtil;
    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public List<Goal> getAllGoals() {
        // Recupera l'utente corrente
        AppUser user = currentUserUtil.getCurrentUser();
        return goalRepository.findByUser(user);
    }

    public Goal saveGoal(Goal goal) {
        if (goal.getCurrentAmount() == null) goal.setCurrentAmount(0.0);
        return goalRepository.save(goal);
    }

    public void contributeToGoal(Long id, Double amount) {
        Goal goal = goalRepository.findById(id).orElseThrow();
        
        // Aggiorna l'importo
        goal.setCurrentAmount(goal.getCurrentAmount() + amount);
        Goal savedGoal = goalRepository.save(goal);
        
        // Controlla se l'obiettivo è stato completato o è vicino al completamento
        goalNotificationService.checkAndNotifyGoalCompletion(savedGoal);
    }

    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}
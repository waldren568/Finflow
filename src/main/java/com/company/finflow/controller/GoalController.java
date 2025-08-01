package com.company.finflow.controller;

import com.company.finflow.model.Goal;
import com.company.finflow.service.GoalService;
import com.company.finflow.model.AppUser;
import com.company.finflow.service.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GoalController {
    @Autowired
    private CurrentUserUtil currentUserUtil;

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping("/add-goal")
    public String addGoal(@ModelAttribute Goal goal) {
        AppUser user = currentUserUtil.getCurrentUser();
        goal.setUser(user);
        goalService.saveGoal(goal);
        return "redirect:/";
    }

    @PostMapping("/contribute-goal")
    public String contributeToGoal(@RequestParam Long id, @RequestParam Double amount) {
        goalService.contributeToGoal(id, amount);
        return "redirect:/";
    }

    @PostMapping("/delete-goal")
    public String deleteGoal(@RequestParam Long id) {
        goalService.deleteGoal(id);
        return "redirect:/";
    }
}
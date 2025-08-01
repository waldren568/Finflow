package com.company.finflow.controller;

import com.company.finflow.model.*;
import com.company.finflow.service.*;
import com.company.finflow.repository.AppUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    private final CurrentUserUtil currentUserUtil;
    private final AppUserRepository appUserRepository;
    private final BalanceService balanceService;
    private final UserService userService;
    private final GoalService goalService;
    private final SectionService sectionService;

    public HomeController(CurrentUserUtil currentUserUtil,
                         AppUserRepository appUserRepository,
                         BalanceService balanceService,
                         UserService userService,
                         GoalService goalService,
                         SectionService sectionService) {
        this.currentUserUtil = currentUserUtil;
        this.appUserRepository = appUserRepository;
        this.balanceService = balanceService;
        this.userService = userService;
        this.goalService = goalService;
        this.sectionService = sectionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        AppUser user = currentUserUtil.getCurrentUser();
        String theme = (user != null && user.getTheme() != null) ? user.getTheme() : "light";
        model.addAttribute("user", user);
        model.addAttribute("theme", theme); // Passa il tema alla view
        model.addAttribute("balance", balanceService.getBalanceForUser(user));
        model.addAttribute("goals", goalService.getAllGoals());
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("newGoal", new Goal());
        model.addAttribute("newSection", new Section());
        return "index";
    }

    @PostMapping("/toggle-theme")
    public String toggleTheme() {
        AppUser user = currentUserUtil.getCurrentUser();
        if (user != null) {
            String newTheme = "light";
            if ("light".equals(user.getTheme())) {
                newTheme = "dark";
            }
            user.setTheme(newTheme);
            appUserRepository.save(user);
        }
        return "redirect:/";
    }
}
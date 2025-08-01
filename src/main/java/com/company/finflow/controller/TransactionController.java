package com.company.finflow.controller;

import com.company.finflow.service.BalanceService;
import com.company.finflow.model.AppUser;
import com.company.finflow.service.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransactionController {
    @Autowired
    private CurrentUserUtil currentUserUtil;

    private final BalanceService balanceService;

    public TransactionController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping("/add-amount")
    public String addAmount(@RequestParam Double amount) {
        if (amount != null && amount > 0) {
            AppUser user = currentUserUtil.getCurrentUser();
            balanceService.addAmountForUser(amount, user);
        }
        return "redirect:/";
    }
}
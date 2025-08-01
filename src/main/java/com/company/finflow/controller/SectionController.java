package com.company.finflow.controller;

import com.company.finflow.model.Section;
import com.company.finflow.service.SectionService;
import com.company.finflow.model.AppUser;
import com.company.finflow.service.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SectionController {
    @PostMapping("/reset-balance")
    public String resetBalance() {
        AppUser user = currentUserUtil.getCurrentUser();
        sectionService.getBalanceService().resetAllSectionsAndBalance(user);
        return "redirect:/";
    }
    @Autowired
    private CurrentUserUtil currentUserUtil;

    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/add-section")
    public String addSection(@ModelAttribute Section section) {
        if (section.getPercentage() != null && section.getPercentage() >= 0 && section.getPercentage() <= 100) {
            AppUser user = currentUserUtil.getCurrentUser();
            section.setUser(user);
            sectionService.saveSection(section);
        }
        return "redirect:/";
    }

    @PostMapping("/section/delete")
    public String deleteSection(@RequestParam Long id) {
        sectionService.deleteSection(id);
        return "redirect:/";
    }

    @PostMapping("/section/rename")
    public String renameSection(@RequestParam Long id, @RequestParam String newName) {
        sectionService.renameSection(id, newName);
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long sectionId, @RequestParam Double amount) {
        sectionService.withdrawFromSection(sectionId, amount);
        return "redirect:/";
    }
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromSectionId, @RequestParam Long toSectionId, @RequestParam Double amount) {
        sectionService.transferBetweenSections(fromSectionId, toSectionId, amount);
        return "redirect:/";
    }
}
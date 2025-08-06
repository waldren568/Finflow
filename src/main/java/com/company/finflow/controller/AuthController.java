package com.company.finflow.controller;

import com.company.finflow.model.AppUser;
import com.company.finflow.service.AppUserService;
import com.company.finflow.repository.AppUserRepository;
import com.company.finflow.service.BalanceService;
import com.company.finflow.service.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Controller
public class AuthController {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private com.company.finflow.service.GoalService goalService;
    @Autowired
    private com.company.finflow.service.SectionService sectionService;
    @Autowired
    private CurrentUserUtil currentUserUtil;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("theme", "light"); // Default per non autenticati
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("theme", "light"); // Default per non autenticati
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        if (appUserService.findByUsername(username) != null || appUserService.findByEmail(email) != null) {
            model.addAttribute("error", "Username o email già esistenti");
            return "register";
        }
        
        appUserService.registerUser(username, email, password);
        
        model.addAttribute("message", "Registration successful! Please check your email to verify your account before logging in.");
        model.addAttribute("messageType", "success");
        return "login";
    }

    @PostMapping("/delete-test-users")
    @Transactional
    public String deleteTestUsers() {
        appUserRepository.deleteByUsernameStartingWith("test");
        return "redirect:/";
    }

    /**
     * Endpoint per cancellare tutti gli account non verificati
     */
    @PostMapping("/delete-unverified-users")
    @Transactional
    public String deleteUnverifiedUsers(Model model) {
        try {
            // Trova tutti gli utenti non verificati
            List<AppUser> unverifiedUsers = appUserRepository.findByEmailVerified(false);
            
            System.out.println("🔍 FOUND " + unverifiedUsers.size() + " UNVERIFIED USERS");
            
            int deletedCount = 0;
            // Cancella ciascun utente non verificato
            for (AppUser user : unverifiedUsers) {
                System.out.println("🗑️ DELETING USER: " + user.getUsername() + " (" + user.getEmail() + ")");
                appUserService.deleteUser(user);
                deletedCount++;
            }
            
            String message = "✅ Successfully deleted " + deletedCount + " unverified accounts";
            System.out.println(message);
            
            return "redirect:/?message=" + java.net.URLEncoder.encode(message, "UTF-8");
            
        } catch (Exception e) {
            String errorMessage = "❌ Error deleting unverified accounts: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            
            try {
                return "redirect:/?error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8");
            } catch (Exception ex) {
                return "redirect:/?error=Error occurred while deleting accounts";
            }
        }
    }

    /**
     * Endpoint per contare gli account non verificati (solo visualizzazione)
     */
    @GetMapping("/count-unverified-users")
    @ResponseBody
    public String countUnverifiedUsers() {
        try {
            List<AppUser> unverifiedUsers = appUserRepository.findByEmailVerified(false);
            return "Found " + unverifiedUsers.size() + " unverified accounts in database";
        } catch (Exception e) {
            return "Error counting unverified accounts: " + e.getMessage();
        }
    }

    /**
     * Endpoint per cancellare l'account dell'utente corrente.
     * Cancella automaticamente tutti i dati correlati (goals, sections, balances)
     * grazie al CascadeType.ALL definito nel modello AppUser.
     */
    @PostMapping("/delete-account")
    @Transactional
    public String deleteCurrentUserAccount() {
        AppUser currentUser = currentUserUtil.getCurrentUser();
        
        if (currentUser != null) {
            // Invalidare la sessione corrente
            SecurityContextHolder.clearContext();
            
            // Cancellare l'utente dal database
            // Questo cancellerà automaticamente anche goals, sections e balances
            // grazie al CascadeType.ALL nelle relazioni OneToMany
            appUserService.deleteUser(currentUser);
        }
        
        // Reindirizza alla pagina di login con messaggio di conferma
        return "redirect:/login?deleted";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String verificationToken, Model model) {
        boolean isVerified = appUserService.verifyUser(verificationToken);
        
        // Aggiungi il risultato della verifica al modello
        model.addAttribute("verified", isVerified);
        
        // Reindirizza alla pagina dedicata per la verifica
        return "email-verification";
    }
}
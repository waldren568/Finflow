package com.company.finflow.service;

import com.company.finflow.model.AppUser;
import com.company.finflow.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IEmailService emailService;

    public AppUser registerUser(String username, String email, String rawPassword) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setTheme("light"); // Tema di default per ogni nuovo utente
        user.setEmailVerified(false); // Email non ancora verificata
        user.setVerificationToken(generateVerificationToken()); // Genera token di verifica
        
        AppUser savedUser = appUserRepository.save(user);
        
        // Invia email di verifica
        emailService.sendVerificationEmail(email, user.getVerificationToken());
        System.out.println("Verification token for " + email + ": " + user.getVerificationToken());
        
        return savedUser;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public boolean verifyUser(String verificationToken) {
        AppUser user = appUserRepository.findByVerificationToken(verificationToken).orElse(null);
        if (user != null) {
            user.setEmailVerified(true);
            user.setVerificationToken(null); // Rimuovi il token dopo la verifica
            appUserRepository.save(user);
            return true;
        }
        return false;
    }

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username).orElse(null);
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email).orElse(null);
    }

    /**
     * Cancella l'account dell'utente specificato dal database.
     * Grazie al CascadeType.ALL nelle relazioni OneToMany in AppUser,
     * vengono automaticamente cancellati anche tutti i dati correlati
     * (goals, sections, balances).
     * 
     * @param user l'utente da cancellare
     */
    public void deleteUser(AppUser user) {
        appUserRepository.delete(user);
    }

    /**
     * Cancella l'account dell'utente specificato dall'ID.
     * 
     * @param userId l'ID dell'utente da cancellare
     */
    public void deleteUserById(Long userId) {
        appUserRepository.deleteById(userId);
    }
}
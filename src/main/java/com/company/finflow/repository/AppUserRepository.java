package com.company.finflow.repository;

import com.company.finflow.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByVerificationToken(String verificationToken);
    void deleteByUsernameStartingWith(String prefix);
    List<AppUser> findByEmailVerified(boolean emailVerified);
}

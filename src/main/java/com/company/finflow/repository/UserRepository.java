package com.company.finflow.repository;

import com.company.finflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    default User getUser() {
        return findAll().stream().findFirst().orElseGet(() -> {
            User user = new User();
            save(user);
            return user;
        });
    }
}
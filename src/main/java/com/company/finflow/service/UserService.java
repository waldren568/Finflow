package com.company.finflow.service;

import com.company.finflow.model.User;
import com.company.finflow.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        return userRepository.getUser();
    }

    public void toggleTheme() {
        User user = userRepository.findAll().stream().findFirst().orElse(new User());
        if ("light".equals(user.getTheme())) {
            user.setTheme("dark");
        } else {
            user.setTheme("light");
        }
        userRepository.save(user);
    }
}
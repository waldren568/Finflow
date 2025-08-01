package com.company.finflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username = "default";
    private String theme = "light"; // "light" o "dark"

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
}
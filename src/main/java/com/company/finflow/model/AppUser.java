package com.company.finflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Goal> goals = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Section> sections = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Balance> balances = new java.util.ArrayList<>();

    public java.util.List<Goal> getGoals() { return goals; }
    public void setGoals(java.util.List<Goal> goals) { this.goals = goals; }
    public java.util.List<Section> getSections() { return sections; }
    public void setSections(java.util.List<Section> sections) { this.sections = sections; }
    public java.util.List<Balance> getBalances() { return balances; }
    public void setBalances(java.util.List<Balance> balances) { this.balances = balances; }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    
    private String theme;
    
    @Column(name = "email_verified")
    private boolean emailVerified = false;
    
    @Column(name = "verification_token")
    private String verificationToken;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTheme() {  return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    
    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
}

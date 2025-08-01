package com.company.finflow.model;

import jakarta.persistence.*;

import com.company.finflow.model.AppUser;

@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double targetAmount;
    private Double currentAmount = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    // Consigliato: opzionale, per evitare orfani
    // @ManyToOne(optional = false)
    private AppUser user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(Double targetAmount) { this.targetAmount = targetAmount; }

    public Double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(Double currentAmount) { this.currentAmount = currentAmount; }

    public Double getProgressPercentage() {
        return targetAmount > 0 ? (currentAmount / targetAmount) * 100 : 0;
    }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
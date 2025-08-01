package com.company.finflow.model;

import jakarta.persistence.*;
import com.company.finflow.model.AppUser;

@Entity
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    // Consigliato: opzionale, per evitare orfani
    // @ManyToOne(optional = false)
    private AppUser user;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
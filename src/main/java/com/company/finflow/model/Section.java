package com.company.finflow.model;

import jakarta.persistence.*;

import com.company.finflow.model.AppUser;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double allocatedAmount = 0.0;
    private Integer percentage; // es. 15 per 15%
    private boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    // Consigliato: opzionale, per evitare orfani
    // @ManyToOne(optional = false)
    private AppUser user;

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(Double allocatedAmount) { this.allocatedAmount = allocatedAmount; }

    public Integer getPercentage() { return percentage; }
    public void setPercentage(Integer percentage) { this.percentage = percentage; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
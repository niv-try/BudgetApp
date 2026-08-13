package com.example.budgetapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("username")
    private String ownerUsername;
    private Long id;
    private double balance;

    public Account() {}

    public Account(String ownerUsername, double balance) {
        this.ownerUsername = ownerUsername;
        this.balance = balance;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
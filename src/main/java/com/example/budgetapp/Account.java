package com.example.budgetapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ownerUsername;
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
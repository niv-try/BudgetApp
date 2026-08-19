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
    private Long id;

    @JsonProperty("username")
    private String ownerUsername;

    private double balance;

    // הוספנו רק את השדה של הבאדג'ים למסד הנתונים!
    private String badges;
    @jakarta.persistence.Column(columnDefinition = "TEXT")
    private String profilePic;

    private int xp = 20;
    private int streak = 1;
    private String lastLogin;

    public Account() {}

    public Account(String ownerUsername, double balance) {
        this.ownerUsername = ownerUsername;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    // Getters & Setters לבאדג'ים
    public String getBadges() { return badges; }
    public void setBadges(String badges) { this.badges = badges; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }

    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }
}
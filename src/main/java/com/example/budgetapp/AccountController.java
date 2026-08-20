package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public Account getBalance() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));
        double currentBalance = account.getBalance();

        List<Transaction> transactions = transactionRepository.findByOwnerUsername(username);
        LocalDate today = LocalDate.now();

        for (Transaction t : transactions) {
            if ("income".equals(t.getType())) {
                currentBalance += t.getAmount();
            } else if ("expense".equals(t.getType()) || "savings".equals(t.getType())) {
                currentBalance -= t.getAmount();
            } else if ("credit_expense".equals(t.getType())) {
                // שימוש ב-toLocalDate() במקום בחיתוך טקסט
                try {
                    LocalDate billingDate = t.getDate().toLocalDate();
                    if (!billingDate.isAfter(today)) { // התאריך הגיע או עבר
                        currentBalance -= t.getAmount();
                    }
                } catch (Exception e) {
                    currentBalance -= t.getAmount();
                }
            }
        }

        account.setBalance(currentBalance);
        return account;
    }

    @PostMapping
    public Account updateBalance(@RequestBody double desiredTotalBalance) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));

        double transactionsTotal = 0;
        List<Transaction> transactions = transactionRepository.findByOwnerUsername(username);
        LocalDate today = LocalDate.now();

        for (Transaction t : transactions) {
            if ("income".equals(t.getType())) {
                transactionsTotal += t.getAmount();
            } else if ("expense".equals(t.getType()) || "savings".equals(t.getType())) {
                transactionsTotal -= t.getAmount();
            } else if ("credit_expense".equals(t.getType())) {
                // שימוש ב-toLocalDate() במקום בחיתוך טקסט
                try {
                    LocalDate billingDate = t.getDate().toLocalDate();
                    if (!billingDate.isAfter(today)) {
                        transactionsTotal -= t.getAmount();
                    }
                } catch (Exception e) {
                    transactionsTotal -= t.getAmount();
                }
            }
        }

        double newBaseBalance = desiredTotalBalance - transactionsTotal;
        account.setBalance(newBaseBalance);

        return repository.save(account);
    }

    @PostMapping("/badges")
    public void saveBadges(@RequestBody List<String> badges) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));

        account.setBadges(String.join(",", badges));
        repository.save(account);
    }

    @PostMapping("/profile")
    public void saveProfile(@RequestBody java.util.Map<String, Object> payload) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));

        if (payload.containsKey("profilePic") && payload.get("profilePic") != null) {
            account.setProfilePic(payload.get("profilePic").toString());
        }
        if (payload.containsKey("xp") && payload.get("xp") != null) {
            account.setXp(((Number) payload.get("xp")).intValue());
        }
        if (payload.containsKey("streak") && payload.get("streak") != null) {
            account.setStreak(((Number) payload.get("streak")).intValue());
        }
        if (payload.containsKey("lastLogin") && payload.get("lastLogin") != null) {
            account.setLastLogin(payload.get("lastLogin").toString());
        }

        repository.save(account);
    }
}
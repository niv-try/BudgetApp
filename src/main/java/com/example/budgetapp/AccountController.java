package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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
        for (Transaction t : transactions) {
            if ("income".equals(t.getType())) {
                currentBalance += t.getAmount();
            } else if ("expense".equals(t.getType()) || "savings".equals(t.getType())) {
                currentBalance -= t.getAmount();
            }
        }

        // מחזירים את האובייקט המקורי כדי שהאתר יקבל גם את המדליות
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
        for (Transaction t : transactions) {
            if ("income".equals(t.getType())) {
                transactionsTotal += t.getAmount();
            } else if ("expense".equals(t.getType()) || "savings".equals(t.getType())) {
                transactionsTotal -= t.getAmount();
            }
        }

        double newBaseBalance = desiredTotalBalance - transactionsTotal;
        account.setBalance(newBaseBalance);

        return repository.save(account);
    }

    // --- הנתיב החדש ששומר אך ורק את המדליות! ---
    @PostMapping("/badges")
    public void saveBadges(@RequestBody List<String> badges) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));

        // הופך את רשימת המדליות לטקסט ושומר בטבלה
        account.setBadges(String.join(",", badges));
        repository.save(account);
    }

    @PostMapping("/profile")
    public void saveProfile(@RequestBody java.util.Map<String, Object> payload) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));

        // אם נשלחה תמונה מהדפדפן, שומרים אותה למשתמש
        if (payload.containsKey("profilePic")) {
            account.setProfilePic((String) payload.get("profilePic"));
        }
        repository.save(account);
    }
}
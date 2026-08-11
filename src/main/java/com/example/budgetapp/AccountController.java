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

        // 1. שליפת העו"ש/יתרת הבסיס שהוגדרה ידנית
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));
        double currentBalance = account.getBalance();

        // 2. שליפת כל העסקאות של המשתמש וחישובן
        List<Transaction> transactions = transactionRepository.findByOwnerUsername(username);
        for (Transaction t : transactions) {
            // אם זו הכנסה, מוסיפים לסכום
            if ("income".equals(t.getType())) {
                currentBalance += t.getAmount();
            }
            // אם זו הוצאה או חיסכון, מורידים מהסכום
            else if ("expense".equals(t.getType()) || "savings".equals(t.getType())) {
                currentBalance -= t.getAmount();
            }
        }

        // מחזירים אובייקט עם הסכום המשוקלל
        return new Account(username, currentBalance);
    }

    @PostMapping
    public Account updateBalance(@RequestBody double newBalance) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));

        account.setBalance(newBalance);
        return repository.save(account);
    }
}
package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountRepository repository;

    @GetMapping
    public Account getBalance() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0)); // אם אין חשבון, מחזיר 0
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
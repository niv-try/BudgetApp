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

        return new Account(username, currentBalance);
    }

    @PostMapping
    public Account updateBalance(@RequestBody double desiredTotalBalance) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = repository.findByOwnerUsername(username)
                .orElse(new Account(username, 0.0));

        // נחשב קודם מה הסך של כל הפעולות הקיימות כרגע
        double transactionsTotal = 0;
        List<Transaction> transactions = transactionRepository.findByOwnerUsername(username);
        for (Transaction t : transactions) {
            if ("income".equals(t.getType())) {
                transactionsTotal += t.getAmount();
            } else if ("expense".equals(t.getType()) || "savings".equals(t.getType())) {
                transactionsTotal -= t.getAmount();
            }
        }

        // הטריק: היתרה ההתחלתית שלך תהיה המספר שביקשת פחות כל הפעולות.
        // כך שכל מספר שתקליד באתר יהפוך מיידית לתוצאה הסופית והמדויקת!
        double newBaseBalance = desiredTotalBalance - transactionsTotal;
        account.setBalance(newBaseBalance);

        return repository.save(account);
    }
}
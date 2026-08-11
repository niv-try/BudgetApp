package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    // שליפת כל הפעולות של המשתמש המחובר בלבד
    @GetMapping
    public List<Transaction> getAllTransactions() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByOwnerUsername(currentUsername);
    }

    // הוספת פעולה חדשה ושיוך שלה למשתמש המחובר
    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        transaction.setOwnerUsername(currentUsername);
        return repository.save(transaction);
    }

    // מחיקת פעולה לפי מזהה
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
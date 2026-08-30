package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // מחיקת פעולה - מאובטחת!
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // קודם מושכים את הפעולה ממסד הנתונים
        Transaction transaction = repository.findById(id).orElse(null);

        // בודקים שהפעולה קיימת, ושהשם של הבעלים תואם לשם של מי שמנסה למחוק
        if (transaction != null && transaction.getOwnerUsername().equals(currentUsername)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build(); // מחזיר סטטוס 200 (הצלחה)
        }

        // אם מישהו מנסה למחוק פעולה של מישהו אחר, או פעולה שלא קיימת
        return ResponseEntity.status(403).body("אין לך הרשאה למחוק פעולה זו!");
    }
}
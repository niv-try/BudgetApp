package com.example.budgetapp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*") // חשוב: מאפשר לקובץ ה-HTML שלנו לגשת לשרת ללא חסימות אבטחה
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    // שליפת כל הפעולות
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    // הוספת פעולה חדשה
    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return repository.save(transaction);
    }

    // מחיקת פעולה לפי מזהה
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
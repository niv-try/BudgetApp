package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin(origins = "*")
public class GoalController {

    @Autowired
    private GoalRepository repository;

    // שליפת היעדים של המשתמש המחובר בלבד
    @GetMapping
    public List<Goal> getAllGoals() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByOwnerUsername(currentUsername);
    }

    // הוספת יעד חדש ושיוך אוטומטי למשתמש המחובר
    @PostMapping
    public Goal addGoal(@RequestBody Goal goal) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        goal.setOwnerUsername(currentUsername);
        return repository.save(goal);
    }

    // מחיקת יעד - מאובטחת!
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // מושכים את היעד ממסד הנתונים
        Goal goal = repository.findById(id).orElse(null);

        // בודקים שהיעד קיים, ושהשם של הבעלים תואם לשם של מי שמנסה למחוק
        if (goal != null && goal.getOwnerUsername().equals(currentUsername)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build(); // סטטוס 200 (הצלחה)
        }

        // זריקת שגיאת הרשאה (403) אם זה לא שלו
        return ResponseEntity.status(403).body("אין לך הרשאה למחוק יעד זה!");
    }
}
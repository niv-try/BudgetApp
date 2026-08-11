package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
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

    // מחיקת יעד לפי מזהה
    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
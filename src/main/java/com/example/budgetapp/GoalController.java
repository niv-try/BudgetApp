package com.example.budgetapp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalRepository repository;

    @GetMapping
    public List<Goal> getAllGoals() {
        return repository.findAll();
    }

    @PostMapping
    public Goal addGoal(@RequestBody Goal goal) {
        return repository.save(goal);
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
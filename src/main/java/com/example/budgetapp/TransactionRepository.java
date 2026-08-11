package com.example.budgetapp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // זהו! Spring Boot יספק לנו אוטומטית פונקציות כמו save(), findAll(), deleteById()
}

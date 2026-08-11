package com.example.budgetapp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // זהו! Spring Boot יספק לנו אוטומטית פונקציות כמו save(), findAll(), deleteById()
    List<Transaction> findByOwnerUsername(String ownerUsername);
}

package com.example.budgetapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/api/ping")
    public String keepAlive() {
        return "Server is awake!"; // לא נוגע בכלל במסד הנתונים!
    }
}
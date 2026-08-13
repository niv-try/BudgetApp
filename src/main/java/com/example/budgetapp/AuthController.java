package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        // בדיקה אם המשתמש כבר קיים
        if(userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("שם המשתמש כבר תפוס");
        }

        // יצירת משתמש חדש והצפנת הסיסמה
        AppUser newUser = new AppUser();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole("ROLE_USER");

        userRepository.save(newUser);
        return ResponseEntity.ok("המשתמש נוצר בהצלחה");
    }
}
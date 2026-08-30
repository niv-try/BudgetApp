package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            // 1. השרת מנסה לאמת את שם המשתמש והסיסמה מול מסד הנתונים
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // אם הסיסמה שגויה, נזרוק שגיאה
            return ResponseEntity.status(401).body("שם משתמש או סיסמה שגויים");
        }

        // 2. אם ההתחברות הצליחה, שולפים את פרטי המשתמש
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // 3. מייצרים את הטוקן בעזרת המפעל שיצרנו קודם
        final String jwt = jwtUtil.generateToken(userDetails);

        // 4. מחזירים את הטוקן ללקוח (לדפדפן)
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}

// --- מחלקות עזר פנימיות להעברת הנתונים (DTOs) ---

class AuthRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class AuthResponse {
    private final String jwt;

    public AuthResponse(String jwt) { this.jwt = jwt; }
    public String getJwt() { return jwt; }
}
package com.example.budgetapp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    // המפתח הסודי שלנו להצפנה (במערכת אמיתית נשמור אותו בקובץ application.properties מוסתר)
    private final String SECRET_KEY = "MySuperSecretKeyForSmartBudgetAppWhichNeedsToBeVeryLongAndSecure";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // חילוץ שם המשתמש מתוך הטוקן
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // חילוץ תאריך התפוגה של הטוקן
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // בדיקה האם הטוקן פג תוקף
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ייצור טוקן חדש למשתמש
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // בניית הטוקן בפועל (מוגדר ל-10 שעות)
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // תוקף ל-10 שעות
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // אימות שהטוקן תקין ושייך למשתמש הנכון
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
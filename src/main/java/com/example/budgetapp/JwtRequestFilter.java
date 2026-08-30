package com.example.budgetapp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. חילוץ ה-Header של האבטחה מהבקשה
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. בדיקה האם יש טוקן והאם הוא מתחיל במילה "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // חותך את המילה Bearer כדי לקבל רק את הטוקן
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // במקרה של טוקן פג תוקף או שגוי, נתעלם וניתן ל-Spring Security לחסום את הבקשה
                System.out.println("טוקן לא תקין או פג תוקף: " + e.getMessage());
            }
        }

        // 3. אם מצאנו שם משתמש בטוקן והמשתמש עדיין לא מחובר למערכת
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // שולפים את המשתמש ממסד הנתונים
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 4. מוודאים שהטוקן תקין ושייך אליו
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // מחברים את המשתמש באופן רשמי למערכת עבור הבקשה הספציפית הזו
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5. מעבירים את הבקשה הלאה לתחנה הבאה (לקונטרולר שלנו)
        chain.doFilter(request, response);
    }
}
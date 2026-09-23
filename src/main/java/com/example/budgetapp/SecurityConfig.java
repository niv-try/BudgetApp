package com.example.budgetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ביטול CSRF כי אנחנו עובדים עם טוקנים
                .authorizeHttpRequests(auth -> auth
                        // פתיחת קבצים סטטיים ועמודי אינטרנט כדי שה-PWA יעבוד למשתמשים לא מחוברים
                        .requestMatchers("/", "/*.html", "/*.js", "/*.css", "/*.png", "/*.ico", "/manifest.json").permitAll()
                        // פתיחת נתיבי ההתחברות וההרשמה
                        .requestMatchers("/api/auth/**", "/api/account/register", "/api/ping", "/error").permitAll()
                        // כל בקשת API אחרת דורשת טוקן תקין!
                        .anyRequest().authenticated()
                )
                // כיבוי זכירת משתמשים (Sessions) - השרת יהיה Stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // הכנסת השומר שלנו לפני השומר הרגיל של Spring
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // הגדרת הצפנת סיסמאות
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
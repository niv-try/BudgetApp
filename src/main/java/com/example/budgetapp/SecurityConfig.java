package com.example.budgetapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // התיקון ה-1: פותח גישה חופשית לגוגל ולכולם לעמוד הבית (index) ולמסכי ההתחברות
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/api/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        // התיקון ה-2: לאחר התחברות מוצלחת, המשתמש מועבר לאפליקציה (dashboard)
                        .defaultSuccessUrl("/dashboard.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        // כשמתנתקים, חוזרים לעמוד הבית הראשי והיפה
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                );

        return http.build();
    }
}
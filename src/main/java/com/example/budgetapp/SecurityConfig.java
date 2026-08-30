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
                        // 1. אישור לעמודי ה-HTML וה-API הציבוריים
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/api/auth/register").permitAll()

                        // 2. הפתרון: אישור גורף לקבצים סטטיים (JS, JSON, תמונות) ולנתיב השגיאות המובנה של Spring
                        .requestMatchers("/*.js", "/*.json", "/*.png", "/*.ico", "/error").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                );

        return http.build();
    }
}
package com.example.budgetapp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // מאפשר לאתר שלנו לשלוח נתונים לשרת בלי להיחסם
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // דורש התחברות עבור כל כניסה לאתר
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/index.html", true) // לאן להעביר אותך אחרי התחברות מוצלחת
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // כאן אנחנו מגדירים את פרטי ההתחברות שלך
        UserDetails nivegozUser = User.builder()
                .username("nivegoz") // שם המשתמש
                .password("{noop}1234") // הסיסמה. התגית {noop} אומרת לשרת לא להצפין אותה כרגע
                .roles("USER")
                .build();
         UserDetails liorUser = User.builder()
                .username("lior") // שם המשתמש
                .password("{noop}4321") // הסיסמה. התגית {noop} אומרת לשרת לא להצפין אותה כרגע
                .roles("USER")
                .build();
        UserDetails leaUser = User.builder()
                .username("leaal") // שם המשתמש
                .password("{noop}5656") // הסיסמה. התגית {noop} אומרת לשרת לא להצפין אותה כרגע
                .roles("USER")
                .build();
        UserDetails sayagUser = User.builder()
                .username("eran29722@gmail.com") // שם המשתמש
                .password("{noop}Sayags$1234") // הסיסמה. התגית {noop} אומרת לשרת לא להצפין אותה כרגע
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(nivegozUser, liorUser, leaUser, sayagUser);
    }
}

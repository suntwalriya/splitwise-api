package com.example.splitwise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/register", "/api/v1/login", "/api/groups/createGroup", "/api/expenses/create", "/api/expenses/settle", "/api/fetch/details/**", "/api/healthCheck").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new UsernamePasswordAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

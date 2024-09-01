package com.example.splitwise.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // TODO: Ensure to add authentication for APIs other than register, login, healthCheck

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection if not required
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/register",
                                "/api/v1/login",
                                "/api/v1/healthCheck",
                                "/api/v1/groups",
                                "/api/v1/expenses",
                                "/api/v1/fetch/details/**"
                        ).permitAll() // Allow unauthenticated access to these APIs
                        .anyRequest().authenticated() // Ensure all other requests are authenticated
                )
                .formLogin(formLogin -> formLogin.disable()) // Disable form login
                .httpBasic(httpBasic -> httpBasic.disable()) // Disable basic HTTP authentication (updated)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")
                        )
                );

        return http.build();
    }
}

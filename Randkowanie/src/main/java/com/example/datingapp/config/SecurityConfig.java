package com.example.datingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // wymóg 5.1 - security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Wyłączamy CSRF dla ułatwienia testów REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/welcome", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll() // Publiczne
                        .requestMatchers("/api/v1/users/register").permitAll() // Pozwalamy każdemu się zarejestrować
                        .requestMatchers("/error").permitAll()      //pokazuje errory
                        .requestMatchers("/api/v1/users/stats/count").permitAll()   //pokazuje liczbe osob w danym miescie
                        .anyRequest().authenticated() // Wszystko inne wymaga zalogowania
                )
                .httpBasic(withDefaults()) // Włączamy proste logowanie (Basic Auth)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())); // Potrzebne do konsoli H2

        return http.build();
    }
}
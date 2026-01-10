package com.example.datingapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatsRepository {
    // Narzędzie do niskopoziomowych zapytań SQL
    private final JdbcTemplate jdbcTemplate; // Wstrzykujemy JdbcTemplate

    public Integer countUsersInCity(String city) {
        String sql = "SELECT COUNT(*) FROM users WHERE city = ?";

        // queryForObject używamy, gdy spodziewamy się jednego wyniku
        return jdbcTemplate.queryForObject(sql, Integer.class, city);
    }
}
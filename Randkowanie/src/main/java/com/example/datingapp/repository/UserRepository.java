package com.example.datingapp.repository;

import com.example.datingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Pozwala logować użytkownika po mailu
    // Daje save(), delete(), findAll()
    Optional<User> findByEmail(String email);
}
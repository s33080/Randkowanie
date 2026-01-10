package com.example.datingapp.repository;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Pozwala logować użytkownika po mailu
    // Daje save(), delete(), findAll()
    // Spring sam wygeneruje SQL na podstawie nazwy metody
    // Select * from users where email = ?
    Optional<User> findByEmail(String email);
}
/*
JpaRepository dostarcza gotowe operacje CRUD (Create, Read, Update, Delete),
więc nie trzeba pisać SQL ręcznie dla prostych zadań
*/

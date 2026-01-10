package com.example.datingapp.repository;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Pozwala logować użytkownika po mailu
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.gender = :gender " +
            "AND u.age BETWEEN :minAge AND :maxAge " +
            "AND (:city IS NULL OR u.city = :city) " +
            "AND u.id != :excludedId " +
            "AND u.id NOT IN (SELECT l.liked.id FROM UserLike l WHERE l.liker.id = :excludedId)"
    )
    List<User> findRecommendedUsers(
            @Param("gender") Gender gender,
            @Param("minAge") int minAge,
            @Param("maxAge") int maxAge,
            @Param("city") String city,
            @Param("excludedId") Long excludedId
    );
}
/*
JpaRepository dostarcza gotowe operacje CRUD (Create, Read, Update, Delete),
więc nie trzeba pisać SQL ręcznie dla prostych zadań
*/

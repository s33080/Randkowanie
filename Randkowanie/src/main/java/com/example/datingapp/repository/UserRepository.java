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

    @Query(value = "SELECT u FROM User u WHERE u.gender = :gender " +
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
    // Znajdź ID wszystkich osób, które polubił dany użytkownik
    @Query(value = "SELECT liked_id FROM user_likes WHERE liker_id = :userId", nativeQuery = true)
    List<Long> findLikedUserIds(@Param("userId") Long userId);

    // Znajdź ID wszystkich osób, które polubiły tego użytkownika
    @Query(value = "SELECT liker_id FROM user_likes WHERE liked_id = :userId", nativeQuery = true)
    List<Long> findUserIdsWhoLikedMe(@Param("userId") Long userId);
}
/*
JpaRepository dostarcza gotowe operacje CRUD (Create, Read, Update, Delete)
*/

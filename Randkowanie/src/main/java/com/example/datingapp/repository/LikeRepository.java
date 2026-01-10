package com.example.datingapp.repository;

import com.example.datingapp.model.User;
import com.example.datingapp.model.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<UserLike, Long> {
    // Sprawdza, czy polubienie już istnieje
    boolean existsByLikerAndLiked(User liker, User liked);

    // Szuka polubienia w drugą stronę (do sprawdzania Matchy)
    Optional<UserLike> findByLikerAndLiked(User liker, User liked);

    @Query("SELECT l.liked FROM UserLike l WHERE l.liker.id = :userId " +
            "AND EXISTS (SELECT 1 FROM UserLike l2 WHERE l2.liker = l.liked AND l2.liked = l.liker)")
    List<User> findAllMatchesForUser(@Param("userId") Long userId);
}
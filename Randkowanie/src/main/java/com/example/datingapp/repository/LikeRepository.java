package com.example.datingapp.repository;

import com.example.datingapp.model.User;
import com.example.datingapp.model.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<UserLike, Long> {
    // Sprawdza, czy polubienie już istnieje
    boolean existsByLikerAndLiked(User liker, User liked);

    // Szuka polubienia w drugą stronę (do sprawdzania Matchy)
    Optional<UserLike> findByLikerAndLiked(User liker, User liked);
}
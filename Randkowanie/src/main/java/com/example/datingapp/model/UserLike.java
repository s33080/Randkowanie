package com.example.datingapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_likes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Wiele polubień może wyjść od jednego użytkownika
    private User liker; // Kto polubił

    @ManyToOne // Wiele osób może polubić tego samego użytkownika
    private User liked; // Kto został polubiony

    @Column(name = "is_rejected")
    private boolean isRejected = false;

    private LocalDateTime createdAt;
}
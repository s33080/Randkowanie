package com.example.datingapp.repository;

import com.example.datingapp.model.ChatMessage;
import com.example.datingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    // Pobiera historię rozmowy między dwoma osobami
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.id = :u1 AND m.recipient.id = :u2) OR " +
            "(m.sender.id = :u2 AND m.recipient.id = :u1) " +
            "ORDER BY m.sentAt ASC")
    List<ChatMessage> findChatHistory(Long u1, Long u2);
}
package com.example.datingapp.service;

import com.example.datingapp.model.ChatMessage;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.LikeRepository;
import com.example.datingapp.repository.MessageRepository;
import com.example.datingapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public String sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User recipient = userRepository.findById(recipientId).orElseThrow();

        // Kluczowa blokada biznesowa:
        boolean isMatch = likeRepository.existsByLikerAndLiked(sender, recipient) &&
                likeRepository.existsByLikerAndLiked(recipient, sender);

        if (!isMatch) {
            return "Błąd: Możesz pisać tylko do osób, z którymi masz dopasowanie (Match)!";
        }

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        return "Wiadomość wysłana!";
    }

    public List<ChatMessage> getChatHistory(Long u1, Long u2) {
        return messageRepository.findChatHistory(u1, u2);
    }
}
package com.example.datingapp.controller;

import com.example.datingapp.model.ChatMessage;
import com.example.datingapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long recipientId,
            @RequestBody String content) {
        return ResponseEntity.ok(chatService.sendMessage(senderId, recipientId, content));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getHistory(
            @RequestParam Long user1,
            @RequestParam Long user2) {
        return ResponseEntity.ok(chatService.getChatHistory(user1, user2));
    }
}
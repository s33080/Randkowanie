package com.example.datingapp.controller;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.User;
import com.example.datingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor // Wstrzykiwanie serwisu przez konstruktor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDTO userDto) {
        User savedUser = userService.registerNewUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // Zwracamy kod 201 Created
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Integer> getCountInCity(@RequestParam String city) {
        return ResponseEntity.ok(userService.getUsersCountByCity(city));
    } //Zwraca liczbę osób w danym mieście na stronę

    @PostMapping("/{likerId}/like/{likedId}")
    public ResponseEntity<String> likeUser(@PathVariable Long likerId, @PathVariable Long likedId) {
        return ResponseEntity.ok(userService.likeUser(likerId, likedId));
    }
}
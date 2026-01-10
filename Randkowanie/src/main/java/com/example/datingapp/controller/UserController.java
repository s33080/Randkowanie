package com.example.datingapp.controller;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.service.RecommendationService;
import com.example.datingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.example.datingapp.service.ImageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor // Wstrzykiwanie serwisu przez konstruktor
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final RecommendationService recommendationService;

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

    @GetMapping("/{id}/matches")
    public ResponseEntity<List<User>> getMatches(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserMatches(id));
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        String fileName = imageService.saveImage(file, id);

        // Używamy już wstrzykniętego userService zamiast repozytorium
        userService.setProfileImage(id, fileName);

        return ResponseEntity.ok("Zdjęcie zapisane jako: " + fileName);
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<User>> getRecommendations(
            @PathVariable Long id,
            @RequestParam(required = false) Gender targetGender,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String city) {

        // To wywołuje logikę z serwisu
        return ResponseEntity.ok(recommendationService.getRecommendations(id, targetGender, minAge, maxAge, city));
    }
}
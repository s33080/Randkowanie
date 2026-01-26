package com.example.datingapp.controller;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import com.example.datingapp.service.RecommendationService;
import com.example.datingapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor // Wstrzykiwanie serwisu przez konstruktor
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

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

    @PatchMapping("/{id}/profile-image")
    public ResponseEntity<String> updateProfileImage(@PathVariable Long id, @RequestBody String imageUrl) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
        return ResponseEntity.ok("Link do zdjęcia został zaktualizowany!");
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Użytkownik o ID " + id + " oraz wszystkie jego dane (lajki, wiadomości) zostały usunięte.");
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(Principal principal, HttpServletRequest request) throws ServletException {
        if (principal != null) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email).orElseThrow();

            // USUWANIE: Tu może być problem z kluczami obcymi
            userRepository.delete(user);

            // Wylogowanie użytkownika po usunięciu konta, żeby sesja nie wisiała
            request.logout();
        }
        return "redirect:/welcome?deleted=true";
    }
}
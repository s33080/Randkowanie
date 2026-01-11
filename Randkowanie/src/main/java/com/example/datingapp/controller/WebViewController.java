package com.example.datingapp.controller;

import com.example.datingapp.model.User;
import com.example.datingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // WAŻNE: Tu używamy @Controller, a nie @RestController
@RequiredArgsConstructor
public class WebViewController {

    private final UserService userService;

//    @GetMapping("/")
//    public String index(Model model) {
//        // Przekazujemy listę użytkowników do strony HTML
//        model.addAttribute("users", userService.getAllUsers());
//        return "index"; // Szuka pliku index.html w folderze templates
//    }
    @GetMapping("/")
    public String index(@RequestParam(required = false) Long currentUserId, Model model) {
        List<User> users;
        User currentUser = null;

        if (currentUserId != null) {
            // Używamy Twojego nowego algorytmu rekomendacji!
            users = userService.getSmartRecommendations(currentUserId);
            currentUser = userService.getUserById(currentUserId);
        } else {
            users = userService.getAllUsers();
        }

        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("allUsers", userService.getAllUsers()); // Do przełącznika profili
        return "index";
    }
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        // Lista dostępnych tagów
        model.addAttribute("allInterests", List.of(
                "Sport", "Kino", "Podróże", "Informatyka",
                "Gry", "Książki", "Gotowanie", "Muzyka", "Fotografia",
                "DIY", "Medycyna", "Psychologia", "Zwierzęta", "Psy",
                "Koty", "Akwarystyka", "Wędkowanie", "Motoryzacja"));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // Zapisujemy użytkownika i wracamy na stronę główną
        userService.saveUser(user);
        return "redirect:/?currentUserId=" + user.getId();
    }

    @GetMapping("/matches")
    public String showMatches(@RequestParam Long currentUserId, Model model) {
        User currentUser = userService.getUserById(currentUserId);
        List<User> matches = userService.getMatches(currentUserId);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("matches", matches);
        return "matches";
    }
}
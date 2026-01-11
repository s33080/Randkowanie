package com.example.datingapp.controller;

import com.example.datingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // WAŻNE: Tu używamy @Controller, a nie @RestController
@RequiredArgsConstructor
public class WebViewController {

    private final UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        // Przekazujemy listę użytkowników do strony HTML
        model.addAttribute("users", userService.getAllUsers());
        return "index"; // Szuka pliku index.html w folderze templates
    }
}
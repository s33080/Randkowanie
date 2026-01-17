package com.example.datingapp.controller;

import com.example.datingapp.model.User;
import com.example.datingapp.service.ChatService;
import com.example.datingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebViewController {

    private final UserService userService;
    private final ChatService chatService;

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
            // Używamy algorytmu rekomendacji
//            users = userService.getSmartRecommendations(currentUserId);
//            currentUser = userService.getUserById(currentUserId);
            List<User> recommendations = userService.getSmartRecommendations(currentUserId);
            model.addAttribute("users", recommendations);
            model.addAttribute("currentUser", userService.getUserById(currentUserId));
        } else {
            users = userService.getAllUsers();
        }

        //model.addAttribute("users", users);
        //model.addAttribute("currentUser", currentUser);
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

    @GetMapping("/chat")
    public String openChat(@RequestParam Long from, @RequestParam Long to,
      /*                     @ModelAttribute("chatStatus") String chatStatus,*/
                           Model model) {
        // 1. Pobierz historię z Twojego serwisu
        model.addAttribute("messages", chatService.getChatHistory(from, to));

        // 2. Pobierz dane osób (używamy Twojego userService)
        model.addAttribute("me", userService.getUserById(from));
        model.addAttribute("partner", userService.getUserById(to));
        return "chat"; // musi być plik chat.html
    }

    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam Long from, @RequestParam Long to,
                              @RequestParam String content,
                              RedirectAttributes redirectAttributes) {

        // Wywołujemy Twoją metodę z blokadą isMatch
        String result = chatService.sendMessage(from, to, content);

        // Przekazujemy komunikat (np. o błędzie braku matcha) na stronę
        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/chat?from=" + from + "&to=" + to;
    }

    @GetMapping("/like")
    public String likeUser(@RequestParam Long likerId, @RequestParam Long likedId) {
        // 1. Dodaj lajk do bazy (użyj metody, którą już masz w serwisie)
        userService.likeUser(likerId, likedId);

        // 2. PRZEKIEROWANIE: Wróć na stronę główną jako Ty
        // Dzięki temu getSmartRecommendations odpali się jeszcze raz i ukryje tę osobę
        return "redirect:/?currentUserId=" + likerId;
    }
}
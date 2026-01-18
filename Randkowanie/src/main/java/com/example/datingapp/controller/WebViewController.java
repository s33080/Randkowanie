package com.example.datingapp.controller;

import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import com.example.datingapp.service.ChatService;
import com.example.datingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebViewController {

    private final UserService userService;
    private final ChatService chatService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String showUsers(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/welcome"; // Jeśli nie jest zalogowany, wyślij na stronę startową
        }

        // 1. Principal to obiekt od Spring Security, który trzyma maila zalogowanej osoby
        String email = principal.getName();

        // 2. Znajdź użytkownika w bazie po mailu
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        // 3. Przekaż jego dane i ID do widoku
        model.addAttribute("currentUser", currentUser);

        // 4. Pobierz listę osób do wyświetlenia
        List<User> users = userService.getSmartRecommendations(currentUser.getId());
        model.addAttribute("users", users);

        return "index"; // Twoja nazwa pliku HTML
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome"; //  z przyciskiem "Zaloguj" i "Zarejestruj"
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

    @GetMapping("/dislike")
    public String dislikeUser(@RequestParam Long likerId, @RequestParam Long likedId) {
        userService.rejectUser(likerId, likedId);
        return "redirect:/?currentUserId=" + likerId;
    }

}
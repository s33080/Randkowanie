package com.example.datingapp.controller;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import com.example.datingapp.service.ChatService;
import com.example.datingapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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
    public String showUsers(
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String city,
            Model model, Principal principal) {

        // 1. Jeśli nikt nie jest zalogowany, pokazujemy stronę powitalną (Landing Page)
        if (principal == null) {
            return "welcome"; // Zwraca plik welcome.html bez przekierowania (unikamy pętli!)
        }

        // 2. Jeśli jest zalogowany, pobieramy dane
        User currentUser = userRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("selectedGender", gender);
        model.addAttribute("selectedCity", city);

        List<User> users = userService.getSmartRecommendations(currentUser.getId());

        // 3. Filtrowanie (Java Stream)
        if (gender != null) {
            users = users.stream().filter(u -> u.getGender() == gender).toList();
        }
        if (city != null && !city.isBlank()) {
            users = users.stream().filter(u -> u.getCity().equalsIgnoreCase(city)).toList();
        }

        model.addAttribute("users", users);
        return "index"; // Pokazuje stronę główną dla zalogowanych
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Zwraca login.html
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Pobieramy zalogowanego użytkownika
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

        model.addAttribute("user", user); // Przekazujemy obiekt do formularza
        return "edit-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedData, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        // Aktualizujemy tylko te pola, które chcemy pozwolić edytować
        user.setName(updatedData.getName());
        user.setCity(updatedData.getCity());
        user.setBio(updatedData.getBio());
        user.setProfileImageUrl(updatedData.getProfileImageUrl());
        // Jeśli chcesz pozwolić na zmianę płci/wieku, dodaj je tutaj:
        user.setAge(updatedData.getAge());
        user.setGender(updatedData.getGender());

        userRepository.save(user);
        return "redirect:/?profileUpdated=true"; // Przekierowanie na główną z informacją
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        System.out.println("LOG: Próba usunięcia użytkownika: " + email); // To sprawdzi, czy w ogóle tu wchodzisz

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        // 1. Najpierw usuwamy sesję użytkownika
        try {
            request.logout();
        } catch (ServletException e) {
            System.out.println("LOG: Błąd wylogowywania: " + e.getMessage());
        }

        // 2. Usuwamy z bazy
        userRepository.delete(user);
        System.out.println("LOG: Użytkownik usunięty z bazy.");

        return "redirect:/welcome?deleted=true";
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
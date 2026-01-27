package com.example.datingapp.controller;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import com.example.datingapp.service.ChatService;
import com.example.datingapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebViewController {

    private final UserService userService;
    private final ChatService chatService;
    private final UserRepository userRepository;

    private final List<String >availableInterests = List.of(
            "Sport", "Kino", "Podróże", "Informatyka",
            "Gry", "Książki", "Gotowanie", "Muzyka", "Fotografia",
            "DIY", "Medycyna", "Psychologia", "Zwierzęta", "Psy",
            "Koty", "Akwarystyka", "Wędkowanie", "Motoryzacja");

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
        model.addAttribute("allInterests", availableInterests);
        return "edit-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedData,
                                @RequestParam(value = "selectedInterests", required = false) List<String> selectedInterests,
                                Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        user.setName(updatedData.getName());
        user.setCity(updatedData.getCity());
        user.setBio(updatedData.getBio());
        user.setProfileImageUrl(updatedData.getProfileImageUrl());
        user.setAge(updatedData.getAge());
        user.setGender(updatedData.getGender());

        // Aktualizacja zainteresowań
        if (selectedInterests != null) {
            user.setInterests(new HashSet<>(selectedInterests));
        } else {
            user.setInterests(new HashSet<>()); // Jeśli nic nie zaznaczono, czyścimy listę
        }

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
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDTO());
        model.addAttribute("allInterests", availableInterests);

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserDTO userDto,
                               BindingResult bindingResult,
                               Model model) {

    // Jeśli są błędy (np. wiek < 18), wróć do formularza
    if (bindingResult.hasErrors()) {
        return "register"; // Zwróć widok rejestracji, Spring sam przekaże błędy
    }

    userService.registerNewUser(userDto);
    return "redirect:/login?registered=true";
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
        model.addAttribute("messages", chatService.getChatHistory(from, to));

        // Pobierz dane osób (userService)
        model.addAttribute("me", userService.getUserById(from));
        model.addAttribute("partner", userService.getUserById(to));
        return "chat";
    }

    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam Long from, @RequestParam Long to,
                              @RequestParam String content,
                              RedirectAttributes redirectAttributes) {

        String result = chatService.sendMessage(from, to, content);

        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/chat?from=" + from + "&to=" + to;
    }

    @GetMapping("/like")
    public String likeUser(@RequestParam Long likerId, @RequestParam Long likedId) {
        // Dodaj lajk do bazy
        userService.likeUser(likerId, likedId);

        // PRZEKIEROWANIE: Wróć na stronę główną jako Ty
        // Dzięki temu getSmartRecommendations odpali się jeszcze raz i ukryje tę osobę
        return "redirect:/?currentUserId=" + likerId;
    }

    @GetMapping("/dislike")
    public String dislikeUser(@RequestParam Long likerId, @RequestParam Long likedId) {
        userService.rejectUser(likerId, likedId);
        return "redirect:/?currentUserId=" + likerId;
    }
}
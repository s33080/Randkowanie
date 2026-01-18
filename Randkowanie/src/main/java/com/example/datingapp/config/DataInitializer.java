package com.example.datingapp.config;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Dodajemy dane tylko, jeśli baza jest pusta
        if (userRepository.count() == 0) {

            User u1 = createUser("maria@test.com", "Maria", 23, Gender.FEMALE, "Warszawa",
                    Set.of("Sport", "Kino", "Podróże"), "https://images.unsplash.com/photo-1494790108377-be9c29b29330");

            User u2 = createUser("tomek@test.com", "Tomek", 25, Gender.MALE, "Warszawa",
                    Set.of("Sport", "Informatyka", "Gry"), "https://images.unsplash.com/photo-1500648767791-00dcc994a43e");

            User u3 = createUser("ania@test.com", "Ania", 22, Gender.FEMALE, "Kraków",
                    Set.of("Kino", "Książki", "Gotowanie"), "https://images.unsplash.com/photo-1438761681033-6461ffad8d80");

            User u4 = createUser("piotr@test.com", "Piotr", 27, Gender.MALE, "Warszawa",
                    Set.of("Podróże", "Kino", "Sport"), "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e");

            userRepository.saveAll(Set.of(u1, u2, u3, u4));
            System.out.println("--- Dane testowe zostały załadowane ---");
        }
    }

    private User createUser(String email, String name, int age, Gender gender, String city, Set<String> interests, String photoUrl) {
        return User.builder()
                .email(email)
                .name(name)
                .age(age)
                .gender(gender)
                .city(city)
                .interests(interests)
                .profileImageUrl(photoUrl)
                .password(passwordEncoder.encode("haslo")) // TUTAJ SZYFRUJEMY HASŁO
                .build();
    }

}



/*
Automatyzacja: Za każdym razem, gdy zrobisz docker-compose down -v, nie musisz się martwić o dane – po starcie Maria, Tomek i reszta będą już na Ciebie czekać.

Testy algorytmu: - Maria i Piotr mają 3 wspólne zainteresowania (Sport, Kino, Podróże).

    Maria i Tomek mają 1 wspólne zainteresowanie (Sport).

    Dzięki temu Twój algorytm getSmartRecommendations powinien wyświetlić Piotra wyżej niż Tomka na liście Marii
 */
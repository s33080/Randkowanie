package com.example.datingapp.config;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            User u1 = createUser("maria@test.com", "Maria", 23, Gender.FEMALE, "Warszawa",
                    Set.of("Sport", "Kino", "Podróże"),
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                    "Uwielbiam poranne bieganie i wieczory w kinie studyjnym. Szukam kogoś z pasją!");

            User u2 = createUser("tomek@test.com", "Tomek", 25, Gender.MALE, "Warszawa",
                    Set.of("Sport", "Informatyka", "Gry"),
                    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
                    "Programista z zamiłowania, gracz z wyboru. Szukam kogoś do wspólnych maratonów sci-fi.");

            User u3 = createUser("ania@test.com", "Ania", 22, Gender.FEMALE, "Kraków",
                    Set.of("Kino", "Książki", "Gotowanie"),
                    "https://images.unsplash.com/photo-1438761681033-6461ffad8d80",
                    "Krakowska dusza, która kocha zapach starych książek i włoską kuchnię.");

            User u4 = createUser("piotr@test.com", "Piotr", 27, Gender.MALE, "Warszawa",
                    Set.of("Podróże", "Kino", "Sport"),
                    "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e",
                    "Zawsze w podróży. Od Tatr po Azję. Szukam towarzyszki do kolejnej wyprawy.");

            userRepository.saveAll(List.of(u1, u2, u3, u4));
            System.out.println("--- Dane testowe zostały załadowane ---");
        }
    }

    private User createUser(String email, String name, int age, Gender gender, String city,
                            Set<String> interests, String photoUrl, String bio) {
        return User.builder()
                .email(email)
                .name(name)
                .age(age)
                .gender(gender)
                .city(city)
                .interests(interests)
                .profileImageUrl(photoUrl)
                .bio(bio)
                .password("haslo")
                .build();
    }
}
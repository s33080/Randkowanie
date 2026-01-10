package com.example.datingapp.service;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // To realizuje wstrzykiwanie zależności przez konstruktor (wymóg 3.3)
public class UserService {

    private final UserRepository userRepository;

    @Transactional // Zapewnia spójność danych (wymóg 3.2)
    public User registerNewUser(UserDTO userDto) {
        // Mapujemy DTO na Encję
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword()) // Na razie czysty tekst, potem dodamy szyfrowanie
                .age(userDto.getAge())
                .gender(userDto.getGender())
                .city(userDto.getCity())
                .build();

        return userRepository.save(user);
    }
}
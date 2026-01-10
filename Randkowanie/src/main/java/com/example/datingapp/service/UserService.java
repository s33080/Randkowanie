package com.example.datingapp.service;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // To realizuje wstrzykiwanie zależności przez konstruktor (wymóg 3.3)
public class UserService {

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;

    @Transactional // Zapewnia spójność danych (wymóg 3.2)
    /*
    @Transactional: Gwarantuje, że jeśli w metodzie dzieją się dwie operacje na bazie
    i jedna się nie uda, to obie zostaną wycofane (Rollback)
     */
    public User registerNewUser(UserDTO userDto) {
        // Mapujemy DTO na Encję
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword()) // Szyfrowanie może później
                .age(userDto.getAge())
                .gender(userDto.getGender())
                .city(userDto.getCity())
                .build();

        return userRepository.save(user);
    }

    //Ile jest dostępnych użytkowników w mieście
    public int getUsersCountByCity(String city) {
        return userStatsRepository.countUsersInCity(city);
    }

}
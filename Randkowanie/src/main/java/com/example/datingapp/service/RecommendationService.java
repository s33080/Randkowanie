package com.example.datingapp.service;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;

    public List<User> getRecommendations(Long userId, Gender targetGender, Integer minAge, Integer maxAge, String city) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        Gender finalGender = (targetGender != null) ? targetGender : currentUser.getPreferredGender();

        if (finalGender == null) {
            finalGender = (currentUser.getGender() == Gender.MALE) ? Gender.FEMALE : Gender.MALE;
        }

        int finalMinAge = (minAge != null) ? minAge : currentUser.getPreferredMinAge();
        int finalMaxAge = (maxAge != null) ? maxAge : currentUser.getPreferredMaxAge();
        String finalCity = (city != null && !city.isEmpty()) ? city : currentUser.getPreferredCity();

        // Domyślne widełki wieku
        if (finalMinAge == 0) finalMinAge = 18;
        if (finalMaxAge == 0) finalMaxAge = 99;

        return userRepository.findRecommendedUsers(finalGender, finalMinAge, finalMaxAge, finalCity, userId);
    }
}
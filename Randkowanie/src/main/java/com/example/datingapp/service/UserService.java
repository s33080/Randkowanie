package com.example.datingapp.service;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.User;
import com.example.datingapp.model.UserLike;
import com.example.datingapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor // To realizuje wstrzykiwanie zależności przez konstruktor (wymóg 3.3)
public class UserService {

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final LikeRepository likeRepository;

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

    public int getUsersCountByCity(String city) {
        return userStatsRepository.countUsersInCity(city);
    }


    @Transactional
    public String likeUser(Long likerId, Long likedId) {
        if (likerId.equals(likedId)) return "Jak?";

        User liker = userRepository.findById(likerId).orElseThrow();
        User liked = userRepository.findById(likedId).orElseThrow();

        if (likeRepository.existsByLikerAndLiked(liker, liked)) {
            return "Już polubiłeś tę osobę!";
        }

        UserLike newLike = UserLike.builder()
                .liker(liker)
                .liked(liked)
                .createdAt(LocalDateTime.now())
                .build();

        likeRepository.save(newLike);

        // Sprawdzanie Matcha (czy ta druga osoba też polubiła)
        if (likeRepository.existsByLikerAndLiked(liked, liker)) {
            return "MATCH!";
        }

        return "Polubiono użytkownika!";
    }


//    @Transactional
//    public void setProfileImage(Long userId, String fileName) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
//        user.setImagePath(fileName);
//        // Stare
//    }


    public List<User> getUserMatches(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Użytkownik nie istnieje");
        }
        return likeRepository.findAllMatchesForUser(userId);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Użytkownik o podanym ID nie istnieje");
        }
        userRepository.deleteById(id);
    }
}
package com.example.datingapp.user_service.mockito;

import com.example.datingapp.model.User;
import com.example.datingapp.model.UserLike;
import com.example.datingapp.repository.LikeRepository;
import com.example.datingapp.repository.UserRepository;
import com.example.datingapp.repository.UserStatsRepository;
import com.example.datingapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceLikeAndMatchMockitoTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserStatsRepository userStatsRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnMatchStringWhenMutualLikeOccurs() {
        // ARRANGE: dwóch userów
        Long likerId = 1L;
        Long likedId = 2L;
        User liker = new User(); liker.setId(likerId);
        User liked = new User(); liked.setId(likedId);

        when(userRepository.findById(likerId)).thenReturn(Optional.of(liker));
        when(userRepository.findById(likedId)).thenReturn(Optional.of(liked));

        // Symuluje, że to polubienie jeszcze nie istnieje (żeby przeszło pierwszy if)
        when(likeRepository.existsByLikerAndLiked(liker, liked)).thenReturn(false);

        // Symuluje, że druga osoba JUŻ nas polubiła (dlatego ma być MATCH)
        when(likeRepository.existsByLikerAndLiked(liked, liker)).thenReturn(true);

        // ACT
        String result = userService.likeUser(likerId, likedId);

        // ASSERT
        assertThat(result, is(equalTo("MATCH!")));

        // VERIFY
        // Sprawdza, czy save() zostało wywołane raz (czy polubienie się zapisało)
        verify(likeRepository, times(1)).save(any(UserLike.class));
    }

    @Test
    void shouldReturnLikedStringWhenNoMutualLike() {
        // ARRANGE
        Long likerId = 1L;
        Long likedId = 2L;
        User liker = new User(); liker.setId(likerId);
        User liked = new User(); liked.setId(likedId);

        when(userRepository.findById(likerId)).thenReturn(Optional.of(liker));
        when(userRepository.findById(likedId)).thenReturn(Optional.of(liked));
        when(likeRepository.existsByLikerAndLiked(liker, liked)).thenReturn(false);

        // Tutaj druga osoba NIE polubiła
        when(likeRepository.existsByLikerAndLiked(liked, liker)).thenReturn(false);

        // ACT
        String result = userService.likeUser(likerId, likedId);

        // ASSERT
        assertThat(result, is(equalTo("Polubiono użytkownika!")));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        // ARRANGE
        String email = "nieznany@test.pl";
        // Gdy zapytamy o ten email, zwróć puste Optional
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // ACT & ASSERT
        // Sprawdza, czy rzuca wyjątek UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(email);
        });

        // Upewnia się, że repozytorium było odpytane
        verify(userRepository).findByEmail(email);
    }

}
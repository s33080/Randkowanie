package com.example.datingapp.user_service.fakes;

import com.example.datingapp.dto.UserDTO;
import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserStatsRepository;
import com.example.datingapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserServiceTest {

    private UserService userService;
    private FakeUserRepository fakeUserRepository;
    private FakeLikeRepository fakeLikeRepository;

    @BeforeEach
    void setUp() {
        fakeUserRepository = new FakeUserRepository();
        fakeLikeRepository = new FakeLikeRepository();
        UserStatsRepository userStatsRepository = new UserStatsRepository(null);

        // Wstrzykujemy 3 argumenty
        userService = new UserService(
                fakeUserRepository,
                userStatsRepository,
                fakeLikeRepository
        );
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setName("Anna");
        dto.setEmail("anna@test.pl");
        dto.setPassword("haslo123");
        dto.setAge(23);
        dto.setGender(Gender.FEMALE);
        dto.setCity("Hel");

        // Act
        User result = userService.registerNewUser(dto);

        // Assert
        assertThat(result.getName(), is(equalTo("Anna")));
        assertThat(fakeUserRepository.findAll(), hasSize(1));
    }
}

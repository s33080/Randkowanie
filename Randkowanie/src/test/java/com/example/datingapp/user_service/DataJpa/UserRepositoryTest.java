package com.example.datingapp.user_service.DataJpa;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//Hibernate próbuje rozmawiać z bazą danych H2 (której używamy w testach) tak,
//jakby to był PostgreSQL. domyślny dialekt H2 nie rozumie returning id
@DataJpaTest // uruchamia bazę H2 i konfiguruje Hibernate
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager; // Narzędzie do łatwego wstawiania danych w teście

    @Test
    void shouldFindUserByEmail() {
        // ARRANGE
        // Tworzymy użytkownika i zapisujemy go do bazy H2
        User user = new User();
        user.setEmail("tomek@test.pl");
        user.setName("Tomek");
        user.setPassword("password");
        user.setCity("Kraków");
        user.setAge(25);

        // Używamy entityManager, żeby zapisać do bazy
        entityManager.persist(user);
        entityManager.flush(); // Wymuś zapis

        // ACT
        // Używamy repozytorium, żeby go znaleźć
        Optional<User> foundUser = userRepository.findByEmail("tomek@test.pl");

        // ASSERT
        assertThat(foundUser.isPresent(), is(true));
        assertThat(foundUser.get().getName(), equalTo("Tomek"));
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // ACT
        Optional<User> result = userRepository.findByEmail("nieistnieje@test.pl");

        // ASSERT
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void shouldFindRecommendedUsersBasedOnCriteria() {
        // Arrange: Tworzymy dwóch użytkowników
        User liker = createAndSaveUser("Liker", "liker@test.pl", Gender.MALE, "Warszawa", 25);
        User potentialMatch = createAndSaveUser("Zosia", "zosia@test.pl", Gender.FEMALE, "Warszawa", 22);

        // Act: Szukamy kobiet w wieku 18-30 z Warszawy, wykluczając Liker-a
        List<User> recommended = userRepository.findRecommendedUsers(
                Gender.FEMALE, 18, 30, "Warszawa", liker.getId()
        );

        // Assert
        assertThat(recommended, hasSize(1));
        assertThat(recommended.get(0).getName(), is(equalTo("Zosia")));
    }

    private User createAndSaveUser(String name, String email, Gender gender, String city, int age) {
        User user = User.builder()
                .name(name).email(email).gender(gender)
                .city(city).age(age).password("pass")
                .build();
        return entityManager.persist(user);
    }
}

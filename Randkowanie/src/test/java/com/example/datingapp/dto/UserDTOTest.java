package com.example.datingapp.dto;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserDTOTest {

    @Test
    void shouldReturnCorrectNameWhenGetNameIsCalled() {
        // Arrange
        UserDTO userDto = new UserDTO();
        userDto.setName("Anna");

        // Act & Assert (Hamcrest)
        assertThat(userDto.getName(), is(equalTo("Anna")));
    }

    @Test
    void shouldUpdateCityFieldWhenSetCityIsCalled() {
        // Arrange
        UserDTO userDto = new UserDTO();

        // Act
        userDto.setCity("Poznań");

        // Assert (Hamcrest)
        assertThat(userDto, hasProperty("city", equalTo("Poznań")));
    }

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldHaveValidationErrorsWhenFieldsAreEmpty() {
        // Arrange: @NotBlank będą błędne
        UserDTO userDto = new UserDTO();

        // Act: Uruchamiamy walidator
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDto);

        // Assert: (Hamcrest)
        assertThat(violations.toString(), containsString("Imię jest wymagane"));
    }

    @Test
    void shouldFailWhenAgeIsUnder18() {
        // Arrange
        UserDTO userDto = new UserDTO();
        userDto.setName("Maria");
        userDto.setEmail("maria@test.pl");
        userDto.setCity("Moscow");
        userDto.setAge(15); // Min to 18

        // Act
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDto);

        // Assert
        assertThat(violations, hasSize(greaterThan(0)));
        assertThat(violations.iterator().next().getMessage(), equalTo("Musisz mieć ukończone 18 lat"));
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        // Arrange
        UserDTO userDto = new UserDTO();
        userDto.setEmail("zły-email");

        // Act
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDto);

        // Assert
        assertThat(violations.toString(), containsString("To musi być poprawny adres e-mail"));
    }

    @Test
    void shouldMapDtoToEntityUsingUserBuilder() {
        UserDTO dto = new UserDTO();
        dto.setName("Paweł");
        dto.setEmail("pawel@test.pl");
        dto.setPassword("password");
        dto.setAge(23);
        dto.setGender(Gender.MALE);
        dto.setCity("Hel");


        // Act:
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .age(dto.getAge())
                .gender(dto.getGender())
                .city(dto.getCity())
                .build();

        // Assert
        assertThat(user.getName(), is(equalTo("Paweł")));
        assertThat(user.getEmail(), is(equalTo("pawel@test.pl")));
        assertThat(user.getPassword(), is(equalTo("password")));
        assertThat(user.getAge(), is(equalTo(23)));
        assertThat(user.getGender(), is(equalTo(Gender.MALE)));
        assertThat(user.getCity(), is(equalTo("Hel")));
    }

}
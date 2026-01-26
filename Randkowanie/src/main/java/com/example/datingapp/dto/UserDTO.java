package com.example.datingapp.dto;

import com.example.datingapp.model.Gender;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {
    @NotBlank(message = "Imię jest wymagane")
    private String name;

    @Email(message = "To musi być poprawny adres e-mail")
    @NotBlank(message = "E-mail nie może być pusty")
    private String email;

    @Size(min = 5, message = "Hasło musi mieć minimum 5 znaków")
    private String password;

    @Min(value = 18, message = "Musisz mieć ukończone 18 lat")
    @Max(value = 120, message = "Niepoprawny wiek")
    private int age;

    @NotNull(message = "Płeć jest wymagana")
    private Gender gender; // FEMALE / MALE / OTHER

    @NotBlank(message = "Miasto jest wymagane")
    private String city;

    private String bio;

    private String profileImageUrl;

    private Set<String> interests = new HashSet<>();
}
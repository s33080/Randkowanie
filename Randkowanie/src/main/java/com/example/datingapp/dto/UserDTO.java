package com.example.datingapp.dto;

import com.example.datingapp.model.Gender;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserDTO {
    @NotBlank(message = "Imię jest wymagane")
    private String name;

    @Email(message = "To musi być poprawny adres e-mail")
    @NotBlank(message = "E-mail nie może być pusty")
    private String email;

    @Size(min = 6, message = "Hasło musi mieć minimum 6 znaków")
    private String password;

    @Min(value = 18, message = "Musisz mieć ukończone 18 lat")
    @Max(value = 120, message = "Niepoprawny wiek")
    private int age;

    @NotNull(message = "Płeć jest wymagana")
    private Gender gender;

    @NotBlank(message = "Miasto jest wymagane")
    private String city;

    private String profileImageUrl;
}
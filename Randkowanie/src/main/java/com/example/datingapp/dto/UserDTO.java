package com.example.datingapp.dto;

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

    @NotBlank(message = "Płeć jest wymagana")
    private String gender; //  MALE, FEMALE, OTHER

    @NotBlank(message = "Miasto jest wymagane")
    private String city;
}
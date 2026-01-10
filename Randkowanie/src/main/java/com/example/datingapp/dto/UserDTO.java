package com.example.datingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank(message = "Imię jest wymagane")
    private String name;

    @Email(message = "Niepoprawny adres e-mail")
    @NotBlank(message = "E-mail nie może być pusty")
    private String email;

    @Size(min = 6, message = "Hasło musi mieć minimum 6 znaków")
    private String password;
}
package com.example.datingapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Niepoprawny format adresu e-mail")
    @NotBlank(message = "E-mail nie może być pusty")
    @Column(nullable = false, unique = true) //unikalny email o wzorze coś@jakiśmail.com
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;
    private int age;
    private String gender; // płeć
    private String city;

    @Column(length = 1000)
    private String bio;

    private String photoUrl;

    private String interests;
}

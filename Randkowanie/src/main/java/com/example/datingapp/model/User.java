package com.example.datingapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity // Mówi Springowi, że ta klasa to tabela w bazie danych (wymóg 1.1)
@Table(name = "users") // Nadaje nazwę tabeli w SQL
// Lombok: generuje metody dostępowe, by nie pisać ich ręcznie
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // User.builder().name("Mary").build()
public class User {

    @Id //klucz główny
    @GeneratedValue(strategy = GenerationType.IDENTITY) //numeracja automatyczna
    private Long id;

    @Email(message = "Niepoprawny format adresu e-mail") // Walidacja formatu e-mail (wymóg 3.1)
    @NotBlank(message = "E-mail nie może być pusty")    //jakby ktoś nie wpisał nic
    @Column(nullable = false, unique = true) //unikalny email o wzorze coś@jakiśmail.com, unikalny
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;
    private int age;

    @Enumerated(EnumType.STRING) // W bazie zostanie zapisane "MALE" zamiast liczby 0
    private Gender gender;

    private String city;

    @Column(length = 1000)
    private String bio;

    private String photoUrl;

    private String interests;

    //Search preferences
    private Gender preferredGender;
    private int preferredMinAge;
    private int preferredMaxAge;
    private String preferredCity;
}

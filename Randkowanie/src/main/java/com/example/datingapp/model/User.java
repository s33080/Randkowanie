package com.example.datingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @NotBlank(message = "E-mail nie może być pusty")
    @Column(nullable = false, unique = true) //unikalny email o wzorze coś@jakiśmail.com
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

    private String profileImageUrl;

    @ElementCollection(fetch = FetchType.EAGER) // Eager, żebyśmy zawsze mieli tagi pod ręką
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest")
    private Set<String> interests = new HashSet<>();

    //Search preferences
    @Enumerated(EnumType.STRING) //męczybuła
    private Gender preferredGender;

    private int preferredMinAge;

    private int preferredMaxAge;

    private String preferredCity;

    // SNAP THANOSA

    // Kaskadowo usuwa polubienia, które wysłał ten użytkownik
    @JsonIgnore // Przeklęta pętla naprawiona
    @OneToMany(mappedBy = "liker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLike> sentLikes = new ArrayList<>();

    // Kaskadowo usuwa polubienia, które ten użytkownik otrzymał
    @JsonIgnore
    @OneToMany(mappedBy = "liked", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLike> receivedLikes = new ArrayList<>();

    // Kaskadowo usuwa wysłane wiadomości
    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> sentMessages = new ArrayList<>();

    // Kaskadowo usuwa odebrane wiadomości
    @JsonIgnore
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> receivedMessages = new ArrayList<>();

    /*
    Orphan Removal oraz CascadeType.ALL. Dzięki temu system automatycznie
    dba o spójność danych (referential integrity) na poziomie aplikacji
     */
}

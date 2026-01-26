package com.example.datingapp.user_service.fakes;

import com.example.datingapp.model.Gender;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;

// Ta klasa udaje bazę danych w pamięci RAM
public class FakeUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 1;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // Symulujemy generowanie ID przez bazę danych
            user.setId(idCounter++);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findRecommendedUsers(Gender gender, int minAge, int maxAge, String city, Long excludedId) {
        return List.of();
    }

    @Override
    public List<Long> findLikedUserIds(Long userId) {
        return List.of();
    }

    @Override
    public List<Long> findUserIdsWhoLikedMe(Long userId) {
        return List.of();
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    // Musisz zaimplementować pozostałe metody interfejsu (możesz zostawić puste lub rzucić wyjątek)
    // IntelliJ podpowie Ci: "Implement methods"
    @Override public List<User> findAll() { return new ArrayList<>(users.values()); }

    @Override
    public List<User> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override public Optional<User> findById(Long id) { return Optional.ofNullable(users.get(id)); }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override public void deleteById(Long id) { users.remove(id); }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public User getOne(Long aLong) {
        return null;
    }

    @Override
    public User getById(Long aLong) {
        return null;
    }

    @Override
    public User getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }
}
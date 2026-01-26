package com.example.datingapp.user_service.fakes;

import com.example.datingapp.model.User;
import com.example.datingapp.model.UserLike;
import com.example.datingapp.repository.LikeRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FakeLikeRepository implements LikeRepository {
    private List<UserLike> likes = new ArrayList<>();

    @Override
    public boolean existsByLikerAndLiked(User liker, User liked) {
        return likes.stream()
                .anyMatch(l -> l.getLiker().equals(liker) && l.getLiked().equals(liked));
    }

    @Override
    public Optional<UserLike> findByLikerAndLiked(User liker, User liked) {
        return likes.stream()
                .filter(l -> l.getLiker().equals(liker) && l.getLiked().equals(liked))
                .findFirst();
    }


    @Override
    public List<User> findAllMatchesForUser(Long userId) {
        // Uproszczona logika na potrzeby testu:
        return new ArrayList<>();
    }

    // Metody z JpaRepository (wymagane przez interfejs)
    @Override public <S extends UserLike> S save(S entity) { likes.add(entity); return entity; }

    @Override
    public void flush() {

    }

    @Override
    public <S extends UserLike> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends UserLike> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<UserLike> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserLike getOne(Long aLong) {
        return null;
    }

    @Override
    public UserLike getById(Long aLong) {
        return null;
    }

    @Override
    public UserLike getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends UserLike> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserLike> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends UserLike> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends UserLike> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserLike> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserLike> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends UserLike, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends UserLike> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<UserLike> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<UserLike> findAll() {
        return List.of();
    }

    @Override
    public List<UserLike> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(UserLike entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserLike> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<UserLike> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<UserLike> findAll(Pageable pageable) {
        return null;
    }
    // ... reszta metod (puste lub rzucające UnsupportedOperationException)
}
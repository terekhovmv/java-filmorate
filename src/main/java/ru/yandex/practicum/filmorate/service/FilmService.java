package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DefaultStorageQualifiers;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    public FilmService(
            FilmStorage filmStorage,
            @Qualifier(DefaultStorageQualifiers.USER)
            UserStorage userStorage,
            LikesStorage likesStorage
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public List<Film> getAll() {
        return filmStorage.stream().collect(Collectors.toList());
    }

    public Film create(Film archetype) {
        Film item = filmStorage.create(archetype);
        log.info("Film {} was successfully added with id {}", item.getName(), item.getId());
        return item;
    }

    public Film update(Film from) {
        Film item = filmStorage.update(from);
        log.info("Film {} was successfully updated", item.getId());
        return item;
    }

    public void addFilmLike(int filmId, long userId) {
        filmStorage.requireContains(filmId);
        requireUser(userId);

        if (likesStorage.addFilmLike(filmId, userId)) {
            log.info("Film {} was successfully liked by user {}", filmId, userId);
        } else {
            log.info("Film {} is already liked by user {}", filmId, userId);
        }
    }

    public void deleteFilmLike(int filmId, long userId) {
        filmStorage.requireContains(filmId);
        requireUser(userId);

        if (likesStorage.deleteFilmLike(filmId, userId)) {
            log.info("Film {} was successfully unliked by user {}", filmId, userId);
        } else {
            log.info("Unable to unlike the film {}, it is not liked by user {}", filmId, userId);
        }
    }

    public List<Film> getPopular(int count) {
        return filmStorage
                .stream()
                .sorted((a,b) -> Integer.compare(likesStorage.getFilmLikesCount(b.getId()), likesStorage.getFilmLikesCount(a.getId())))
                .limit(count)
                .collect(Collectors.toList());
    }

    private User requireUser(long id) {
        return userStorage.getById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}

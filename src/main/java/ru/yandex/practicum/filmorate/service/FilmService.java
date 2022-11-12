package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
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
            @Qualifier(DefaultStorageQualifiers.FILM)
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
        return requireFilm(id);
    }

    public List<Film> getAll() {
        return filmStorage.stream().collect(Collectors.toList());
    }

    public Film create(Film archetype) {
        Film created = filmStorage.create(archetype).orElseThrow(
                () -> new RuntimeException("Unable to create the film " + archetype.getName()));
        log.info("Film {} was successfully added with id {}", created.getName(), created.getId());
        return created;
    }

    public Film update(Film from) {
        requireFilm(from.getId());

        Film updated = filmStorage.update(from).orElseThrow(
                () -> new RuntimeException("Unable to update the film #" + from.getId()));
        log.info("Film {} was successfully updated", updated.getId());
        return updated;
    }

    public void addLike(int filmId, long userId) {
        requireFilm(filmId);
        requireUser(userId);

        if (likesStorage.addLike(filmId, userId)) {
            log.info("Film {} was successfully liked by user {}", filmId, userId);
        } else {
            log.info("Film {} is already liked by user {}", filmId, userId);
        }
    }

    public void deleteLike(int filmId, long userId) {
        requireFilm(filmId);
        requireUser(userId);

        if (likesStorage.deleteLike(filmId, userId)) {
            log.info("Film {} was successfully unliked by user {}", filmId, userId);
        } else {
            log.info("Unable to unlike the film {}, it is not liked by user {}", filmId, userId);
        }
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    private User requireUser(long id) {
        return userStorage.getById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private Film requireFilm(int id) {
        return filmStorage.getById(id).orElseThrow(() -> new FilmNotFoundException(id));
    }
}

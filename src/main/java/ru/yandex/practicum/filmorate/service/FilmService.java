package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    public FilmService(
            FilmStorage filmStorage,
            UserStorage userStorage,
            LikesStorage likesStorage
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film archetype) {
        return filmStorage.create(archetype);
    }

    public Film update(Film from) {
        return filmStorage.update(from);
    }
}

package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
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

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
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
}

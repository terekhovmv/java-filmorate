package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    boolean contains(int id);
    Optional<Film> get(int id);
    default void requireContains(int id) {
        if (!contains(id)) {
            throw new FilmNotFoundException(id);
        }
    }
    default Film require(int id) {
        return get(id).orElseThrow(() -> new FilmNotFoundException(id));
    }

    List<Film> getAll();
    List<Film> getPopular(int count);
    Optional<Film> create(Film archetype);
    Optional<Film> update(Film from);
}

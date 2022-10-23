package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.stream.Stream;

public interface FilmStorage {
    boolean contains(int id);
    Film getById(int id);
    Stream<Film> stream();
    Film create(Film archetype);
    Film update(Film from);

    default void requireContains(int id) {
        if (!contains(id)) {
            throw new FilmNotFoundException(id);
        }
    }
}

package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.stream.Stream;

public interface MpaStorage {
    boolean contains(short id);
    Mpa getById(short id);
    Stream<Mpa> stream();

    default void requireContains(short id) {
        if (!contains(id)) {
            throw new FilmNotFoundException(id);
        }
    }
}
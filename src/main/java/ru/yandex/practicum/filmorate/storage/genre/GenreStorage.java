package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.stream.Stream;

public interface GenreStorage {
    boolean contains(short id);
    Genre getById(short id);
    Stream<Genre> stream();

    default void requireContains(short id) {
        if (!contains(id)) {
            throw new GenreNotFoundException(id);
        }
    }
}
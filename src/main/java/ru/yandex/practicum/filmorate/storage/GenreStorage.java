package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    boolean contains(short id);
    Optional<Genre> get(short id);
    default void requireContains(short id) {
        if (!contains(id)) {
            throw new GenreNotFoundException(id);
        }
    }
    default Genre require(short id) {
        return get(id).orElseThrow(() -> new GenreNotFoundException(id));
    }

    List<Genre> getAll();
}
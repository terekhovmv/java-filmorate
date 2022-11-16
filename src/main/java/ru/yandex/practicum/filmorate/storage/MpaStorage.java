package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    boolean contains(short id);
    Optional<Mpa> get(short id);
    default void requireContains(short id) {
        if (!contains(id)) {
            throw new MpaNotFoundException(id);
        }
    }
    default Mpa require(short id) {
        return get(id).orElseThrow(() -> new MpaNotFoundException(id));
    }

    List<Mpa> getAll();
}
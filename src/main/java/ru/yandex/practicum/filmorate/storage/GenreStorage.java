package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;
import java.util.stream.Stream;

public interface GenreStorage {
    Optional<Genre> getById(short id);
    Stream<Genre> stream();
}
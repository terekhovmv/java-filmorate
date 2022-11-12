package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface FilmStorage {
    Optional<Film> getById(int id);

    Stream<Film> stream();

    Optional<Film> create(Film archetype);

    Optional<Film> update(Film from);

    List<Film> getPopular(int count);
}

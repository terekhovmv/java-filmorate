package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> getById(int id);

    List<Film> getAll();

    List<Film> getPopular(int count);

    Optional<Film> create(Film archetype);

    Optional<Film> update(Film from);
}

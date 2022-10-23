package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Stream;

public interface FilmStorage {
    boolean contains(int id);
    Film getById(int id);
    Stream<Film> stream();
    Film create(Film archetype);
    Film update(Film from);
}

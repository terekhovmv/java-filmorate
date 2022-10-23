package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    boolean contains(int id);
    Film getById(int id);
    List<Film> getAll();
    Film create(Film archetype);
    Film update(Film from);
}

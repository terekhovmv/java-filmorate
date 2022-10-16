package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();
    Film create(Film archetype);
    Film update(Film from);
    void delete(int id);
}

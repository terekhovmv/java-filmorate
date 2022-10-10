package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public class FilmStorage extends AbstractStorage<Film> {
    protected int getId(Film stored) {
        return stored.getId();
    }

    protected Film create(int id, Film archetype) {
        return archetype.toBuilder().id(id).build();
    }
}

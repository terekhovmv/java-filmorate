package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
public class FilmStorage extends AbstractStorage<Film> {
    @Override
    protected int getId(Film stored) {
        return stored.getId();
    }

    @Override
    protected Film create(int id, Film archetype) {
        return archetype.toBuilder().id(id).build();
    }

    @Override
    protected void onUnknown(int id) throws UnknownItem {
        String message = String.format("Unknown film %d requested", id);
        log.warn(message);
        throw new UnknownItem(message);
    }

    @Override
    protected void onAfterCreate(Film created) {
        log.info("Film {} was successfully added with id {}", created.getName(), created.getId());
    }

    @Override
    protected void onAfterUpdate(Film updated) {
        log.info("Film {} was successfully updated", updated.getId());
    }
}

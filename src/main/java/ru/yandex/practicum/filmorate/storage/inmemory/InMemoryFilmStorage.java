package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> {
    @Override
    protected int getId(Film stored) {
        return stored.getId();
    }

    @Override
    protected Film build(int id, Film archetype) {
        return archetype.toBuilder().id(id).build();
    }

    @Override
    protected Film buildForUpdate(Film from) {
        return from;
    }

    @Override
    protected void onUnknown(int id) {
        String message = String.format("Unknown film %d requested", id);
        log.warn(message);
        throw new UnknownItem(message);
    }

    @Override
    protected void onAfterCreate(Film item) {
        log.info("Film {} was successfully added with id {}", item.getName(), item.getId());
    }

    @Override
    protected void onAfterUpdate(Film item) {
        log.info("Film {} was successfully updated", item.getId());
    }

    @Override
    protected void onAfterDelete(Film item) {
        log.info("Film {} was successfully deleted", item.getId());
    }
}

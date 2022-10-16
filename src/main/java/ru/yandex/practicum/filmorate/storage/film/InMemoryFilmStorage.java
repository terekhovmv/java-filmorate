package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> storage = new HashMap<>();
    private int lastId = 0;

    public List<Film> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Film create(Film archetype) {
        lastId++;
        Film item = archetype.toBuilder().id(lastId).build();

        storage.put(lastId, item);
        log.info("Film {} was successfully added with id {}", item.getName(), item.getId());

        return item;
    }

    public Film update(Film from) {
        int id = from.getId();
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        storage.put(id, from);
        log.info("Film {} was successfully updated", from.getId());

        return from;
    }

    public void delete(int id) {
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        Film item = storage.get(id);
        storage.remove(id);
        log.info("Film {} was successfully deleted", item.getId());
    }

    private void onUnknown(int id) {
        String message = String.format("Unknown film %d requested", id);
        log.warn(message);
        throw new UnknownItem(message);
    }
}

package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> storage = new HashMap<>();
    private int lastId = 0;

    @Override
    public boolean contains(int id) {
        return storage.containsKey(id);
    }

    @Override
    public Film getById(int id) {
        checkIsKnown(id);

        return storage.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Film create(Film archetype) {
        lastId++;
        Film item = archetype.toBuilder().id(lastId).build();

        storage.put(lastId, item);
        return item;
    }

    @Override
    public Film update(Film from) {
        int id = from.getId();
        checkIsKnown(id);

        storage.put(id, from);
        return from;
    }

    @Override
    public Film delete(int id) {
        checkIsKnown(id);

        Film item = storage.get(id);
        storage.remove(id);
        return item;
    }

    private void checkIsKnown(int id) {
        if (!contains(id)) {
            throw new UnknownItem(""); //TODO
        }
    }
}

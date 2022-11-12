package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashMap;
import java.util.stream.Stream;

@Component
public class InMemoryGenreStorage implements GenreStorage {

    private static final HashMap<Short, Genre> storage = new HashMap<>();

    public InMemoryGenreStorage() {
        add(1, "Комедия");
        add(2, "Драма");
        add(3, "Мультфильм");
        add(4, "Боевик");
        add(5, "Приключения");
        add(6, "Фентези");
    }

    @Override
    public boolean contains(short id) {
        return storage.containsKey(id);
    }

    @Override
    public Genre getById(short id) {
        requireContains(id);

        return storage.get(id);
    }

    @Override
    public Stream<Genre> stream() {
        return storage.values().stream();
    }

    private void add(int id, String name) {
        storage.put((short)id, Genre.builder().id((short)id).name(name).build());
    }
}
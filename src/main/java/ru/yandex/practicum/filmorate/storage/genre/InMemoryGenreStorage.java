package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Qualifier("in-memory")
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
    public Optional<Genre> getById(short id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Stream<Genre> stream() {
        return storage.values().stream();
    }

    private void add(int id, String name) {
        storage.put((short)id, Genre.builder().id((short)id).name(name).build());
    }
}
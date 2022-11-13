package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
@Qualifier(InMemoryStorageConsts.QUALIFIER)
public class InMemoryGenreStorage implements GenreStorage {

    private static final HashMap<Short, Genre> storage = new HashMap<>();

    public InMemoryGenreStorage() {
        add(1, "Комедия");
        add(2, "Драма");
        add(3, "Мультфильм");
        add(4, "Боевик");
        add(5, "Приключения");
        add(6, "Биография");
    }

    @Override
    public Optional<Genre> getById(short id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(storage.values());
    }

    private void add(int id, String name) {
        storage.put((short)id, new Genre((short)id, name));
    }
}
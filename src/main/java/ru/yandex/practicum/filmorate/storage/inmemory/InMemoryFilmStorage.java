package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Qualifier(InMemoryStorageConsts.QUALIFIER)
public class InMemoryFilmStorage implements FilmStorage {
    private final InMemoryLikesStorage likesStorage;
    private final Map<Integer, Film> storage = new HashMap<>();
    private int lastId = 0;

    public InMemoryFilmStorage(
        InMemoryLikesStorage likesStorage
    ) {
        this.likesStorage = likesStorage;
    }

    @Override
    public Optional<Film> getById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Stream<Film> stream() {
        return storage.values().stream();
    }

    @Override
    public Optional<Film> create(Film archetype) {
        lastId++;
        Film item = archetype.toBuilder().id(lastId).build();

        storage.put(lastId, item);
        return Optional.of(item);
    }

    @Override
    public Optional<Film> update(Film from) {
        int id = from.getId();
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }

        storage.put(id, from);
        return Optional.of(from);
    }

    @Override
    public List<Film> getPopular(int count) {
        return storage.values()
                .stream()
                .sorted((a,b) -> Integer.compare(likesStorage.getLikesCount(b.getId()), likesStorage.getLikesCount(a.getId())))
                .limit(count)
                .collect(Collectors.toList());
    }
}

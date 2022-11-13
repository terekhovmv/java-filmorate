package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DefaultStorageConsts;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Qualifier(InMemoryStorageConsts.QUALIFIER)
public class InMemoryFilmStorage implements FilmStorage {
    private final InMemoryLikesStorage likesStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final Map<Integer, Film> storage = new HashMap<>();
    private int lastId = 0;

    public InMemoryFilmStorage(
        InMemoryLikesStorage likesStorage,
        @Qualifier(InMemoryStorageConsts.QUALIFIER)
        MpaStorage mpaStorage,
        @Qualifier(InMemoryStorageConsts.QUALIFIER)
        GenreStorage genreStorage
    ) {
        this.likesStorage = likesStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Optional<Film> getById(int id) {
        Film item = storage.get(id);
        if (item == null) {
            return Optional.empty();
        }
        return Optional.of(addCalculatedData(item));
    }

    @Override
    public List<Film> getAll() {
        return storage.values()
                .stream()
                .map(this::addCalculatedData)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getPopular(int count) {
        return storage.values()
                .stream()
                .sorted((a,b) -> Integer.compare(
                        likesStorage.getLikesCount(b.getId()),
                        likesStorage.getLikesCount(a.getId()))
                )
                .limit(count)
                .map(this::addCalculatedData)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Film> create(Film archetype) {
        lastId++;
        Film item = archetype.toBuilder().id(lastId).build();

        storage.put(lastId, item);
        return Optional.of(addCalculatedData(item));
    }

    @Override
    public Optional<Film> update(Film from) {
        int id = from.getId();
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }

        storage.put(id, from);
        return Optional.of(addCalculatedData(from));
    }

    private Film addCalculatedData(Film film) {
        return film.toBuilder()
                .mpa(mpaStorage
                        .getById(film.getMpa().getId())
                        .orElse(null))
                .genres(film.getGenres().stream()
                        .map((genreLight)->genreStorage.getById(genreLight.getId()).orElse(null))
                        .collect(Collectors.toList()))
                .rate(likesStorage.getLikesCount(film.getId()))
                .build();
    }
}

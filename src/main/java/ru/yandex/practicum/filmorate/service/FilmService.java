package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;

    public FilmService(
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            FilmStorage filmStorage,
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            UserStorage userStorage,
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            MpaStorage mpaStorage,
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            GenreStorage genreStorage,
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            LikesStorage likesStorage
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
    }

    public Film getById(int id) {
        return filmStorage.require(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film archetype) {
        Film corrected = correctFilmIfNeeded(archetype);
        mpaStorage.requireContains(corrected.getMpa().getId());
        for(Genre genre: corrected.getGenres()) {
            genreStorage.requireContains(genre.getId());
        }

        Film created = filmStorage.create(corrected).orElseThrow(
                () -> new RuntimeException("Unable to create the film " + corrected.getName()));
        log.info("Film {} was successfully added with id {}", created.getName(), created.getId());
        return created;
    }

    public Film update(Film from) {
        Film corrected = correctFilmIfNeeded(from);

        filmStorage.requireContains(corrected.getId());
        mpaStorage.requireContains(corrected.getMpa().getId());
        for(Genre genre: corrected.getGenres()) {
            genreStorage.requireContains(genre.getId());
        }

        Film updated = filmStorage.update(corrected).orElseThrow(
                () -> new RuntimeException("Unable to update the film #" + corrected.getId()));
        log.info("Film {} was successfully updated", updated.getId());
        return updated;
    }

    public void addLike(int filmId, long userId) {
        filmStorage.requireContains(filmId);
        userStorage.requireContains(userId);

        likesStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, long userId) {
        filmStorage.requireContains(filmId);
        userStorage.requireContains(userId);

        likesStorage.deleteLike(filmId, userId);
        log.info("Film {} was successfully unliked by user {}", filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    private Film correctFilmIfNeeded(Film item) {
        List<Genre> itemGenres = item.getGenres();
        if (itemGenres == null) {
            return item.toBuilder().genres(new ArrayList<>()).build();
        }
        Set<Short> uniqueGenreIds = new HashSet<>();
        List<Genre> uniqueGenres = new ArrayList<>();
        for(Genre genre: itemGenres) {
            if (genre != null && !uniqueGenreIds.contains(genre.getId())) {
                uniqueGenreIds.add(genre.getId());
                uniqueGenres.add(genre);
            }
        }
        if (uniqueGenres.size() != itemGenres.size()) {
            return item.toBuilder().genres(uniqueGenres).build();
        }

        return item;
    }
}

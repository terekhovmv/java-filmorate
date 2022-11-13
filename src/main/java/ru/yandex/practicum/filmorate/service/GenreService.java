package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DefaultStorageConsts;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage storage;

    public GenreService(
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            GenreStorage storage
    ) {
        this.storage = storage;
    }

    public Genre getById(short id) {
        return storage.getById(id).orElseThrow(() -> new GenreNotFoundException(id));
    }

    public List<Genre> getAll() {
        return storage.getAll();
    }
}
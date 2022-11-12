package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage storage;

    public GenreService(
            @Qualifier("db") GenreStorage storage
    ) {
        this.storage = storage;
    }

    public Genre getById(short id) {
        return storage.getById(id);
    }

    public List<Genre> getAll() {
        return storage.stream().collect(Collectors.toList());
    }
}
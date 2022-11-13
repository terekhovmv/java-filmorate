package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DefaultStorageConsts;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage storage;

    public MpaService(
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            MpaStorage storage
    ) {
        this.storage = storage;
    }

    public Mpa getById(short id) {
        return storage.require(id);
    }

    public List<Mpa> getAll() {
        return storage.getAll();
    }
}
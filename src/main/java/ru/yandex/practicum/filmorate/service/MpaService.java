package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.StorageQualifiers;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MpaService {
    private final MpaStorage storage;

    public MpaService(
            @Qualifier(StorageQualifiers.DEFAULT_MPA_STORAGE_QUALIFIER)
            MpaStorage storage
    ) {
        this.storage = storage;
    }

    public Mpa getById(short id) {
        return storage.getById(id).orElseThrow(() -> new MpaNotFoundException(id));
    }

    public List<Mpa> getAll() {
        return storage.stream().collect(Collectors.toList());
    }
}
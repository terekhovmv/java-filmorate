package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MpaService {
    private final MpaStorage storage;

    public MpaService(
            @Qualifier("db") MpaStorage storage
    ) {
        this.storage = storage;
    }

    public Mpa getById(short id) {
        return storage.getById(id);
    }

    public List<Mpa> getAll() {
        return storage.stream().collect(Collectors.toList());
    }
}
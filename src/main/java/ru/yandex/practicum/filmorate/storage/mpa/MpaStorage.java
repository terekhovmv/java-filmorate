package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;
import java.util.stream.Stream;

public interface MpaStorage {
    Optional<Mpa> getById(short id);
    Stream<Mpa> stream();
}
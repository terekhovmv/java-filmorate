package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Qualifier(InMemoryStorageConsts.QUALIFIER)
public class InMemoryMpaStorage implements MpaStorage {

    private static final HashMap<Short, Mpa> storage = new HashMap<>();

    public InMemoryMpaStorage() {
        add(1, "G");
        add(2, "PG");
        add(3, "PG-13");
        add(4, "R");
        add(5, "NC-17");
    }

    @Override
    public Optional<Mpa> getById(short id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Mpa> getAll() {
        return new ArrayList<>(storage.values());
    }

    private void add(int id, String name) {
        storage.put(
                (short)id,
                Mpa.builder().id((short)id).name(name).build()
        );
    }
}

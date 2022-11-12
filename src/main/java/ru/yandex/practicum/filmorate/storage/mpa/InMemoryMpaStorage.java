package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashMap;
import java.util.stream.Stream;

@Component
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
    public boolean contains(short id) {
        return storage.containsKey(id);
    }

    @Override
    public Mpa getById(short id) {
        requireContains(id);

        return storage.get(id);
    }

    @Override
    public Stream<Mpa> stream() {
        return storage.values().stream();
    }

    private void add(int id, String name) {
        storage.put((short)id, Mpa.builder().id((short)id).name(name).build());
    }
}

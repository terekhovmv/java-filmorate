package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractStorage<StoredT> {
    private final Map<Integer, StoredT> storage = new HashMap<>();
    private int lastId = 0;

    public List<StoredT> findAll() {
        return new ArrayList<>(storage.values());
    }

    public StoredT create(StoredT archetype) {
        lastId++;
        StoredT created = create(lastId, archetype);
        storage.put(lastId, created);
        return created;
    }

    public StoredT update(StoredT patched) {
        int id = getId(patched);
        if (!storage.containsKey(id)) {
            //throw
        }
        storage.put(id, patched);
        return patched;
    }

    protected abstract int getId(StoredT stored);
    protected abstract StoredT create(int id, StoredT archetype);
}

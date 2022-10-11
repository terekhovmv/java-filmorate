package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;

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
        onAfterCreate(created);

        return created;
    }

    public StoredT update(StoredT from) throws UnknownItem {
        int id = getId(from);
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        StoredT updated = update(storage.get(id), from);
        storage.put(id, updated);
        onAfterUpdate(updated);

        return from;
    }

    protected abstract int getId(StoredT stored);
    protected abstract StoredT create(int id, StoredT archetype);
    protected StoredT update(StoredT prev, StoredT from) {
        return from;
    }
    protected abstract void onUnknown(int id) throws UnknownItem;
    protected abstract void onAfterCreate(StoredT created);
    protected abstract void onAfterUpdate(StoredT updated);
}

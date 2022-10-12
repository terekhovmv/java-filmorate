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
        StoredT item = build(lastId, archetype);

        storage.put(lastId, item);
        onAfterCreate(item);

        return item;
    }

    public StoredT update(StoredT from) throws UnknownItem {
        int id = getId(from);
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        StoredT item = buildForUpdate(from);
        storage.put(id, item);
        onAfterUpdate(item);

        return item;
    }

    protected abstract int getId(StoredT stored);
    protected abstract StoredT build(int id, StoredT archetype);
    protected abstract StoredT buildForUpdate(StoredT from);
    protected abstract void onUnknown(int id) throws UnknownItem;
    protected abstract void onAfterCreate(StoredT item);
    protected abstract void onAfterUpdate(StoredT item);

}

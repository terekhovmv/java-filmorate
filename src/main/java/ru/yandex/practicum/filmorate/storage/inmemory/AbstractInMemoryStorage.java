package ru.yandex.practicum.filmorate.storage.inmemory;

import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractInMemoryStorage<ItemT> implements Storage<ItemT> {
    private final Map<Integer, ItemT> storage = new HashMap<>();
    private int lastId = 0;

    public List<ItemT> findAll() {
        return new ArrayList<>(storage.values());
    }

    public ItemT create(ItemT archetype) {
        lastId++;
        ItemT item = build(lastId, archetype);

        storage.put(lastId, item);
        onAfterCreate(item);

        return item;
    }

    public ItemT update(ItemT from) {
        int id = getId(from);
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        ItemT item = buildForUpdate(from);
        storage.put(id, item);
        onAfterUpdate(item);

        return item;
    }

    public void delete(int id) {
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        ItemT item = storage.get(id);
        storage.remove(id);
        onAfterDelete(item);
    }

    protected abstract int getId(ItemT stored);
    protected abstract ItemT build(int id, ItemT archetype);
    protected abstract ItemT buildForUpdate(ItemT from);
    protected abstract void onUnknown(int id);
    protected abstract void onAfterCreate(ItemT item);
    protected abstract void onAfterUpdate(ItemT item);
    protected abstract void onAfterDelete(ItemT item);
}

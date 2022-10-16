package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface Storage<ItemT> {
    List<ItemT> findAll();
    ItemT create(ItemT archetype);
    ItemT update(ItemT from);
    void delete(int id);
}
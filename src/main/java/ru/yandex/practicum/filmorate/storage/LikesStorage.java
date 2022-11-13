package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    boolean contains(int filmId, long userId);
    void addLike(int filmId, long userId);
    void deleteLike(int filmId, long userId);
}
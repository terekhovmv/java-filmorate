package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    void addLike(int filmId, long userId);
    void deleteLike(int filmId, long userId);
}
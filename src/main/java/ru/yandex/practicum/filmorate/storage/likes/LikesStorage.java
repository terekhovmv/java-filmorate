package ru.yandex.practicum.filmorate.storage.likes;

public interface LikesStorage {
    boolean addLike(int filmId, long userId);
    boolean deleteLike(int filmId, long userId);
}
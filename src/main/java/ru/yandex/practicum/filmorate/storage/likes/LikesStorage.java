package ru.yandex.practicum.filmorate.storage.likes;

public interface LikesStorage {
    void addFilmLike(int filmId, long userId);
    void deleteFilmLike(int filmId, long userId);
    int getFilmLikesCount(int filmId);
}
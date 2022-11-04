package ru.yandex.practicum.filmorate.storage.likes;

public interface LikesStorage {
    boolean addFilmLike(int filmId, long userId);
    boolean deleteFilmLike(int filmId, long userId);
    int getFilmLikesCount(int filmId);
}
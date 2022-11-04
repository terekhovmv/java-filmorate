package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryLikesStorage implements LikesStorage {
    private final Map<Integer, Set<Long>> storage = new HashMap<>();

    @Override
    public boolean addFilmLike(int filmId, long userId) {
        storage.putIfAbsent(filmId, new HashSet<>());
        return storage.get(filmId).add(userId);
    }

    @Override
    public boolean deleteFilmLike(int filmId, long userId) {
        Set<Long> likes = storage.get(filmId);
        if (likes == null) {
            return false;
        }
        boolean result = likes.remove(userId);
        if (result && likes.isEmpty()) {
            storage.remove(filmId);
        }
        return result;
    }

    @Override
    public int getFilmLikesCount(int filmId) {
        Set<Long> likes = storage.get(filmId);
        return (likes == null) ? 0 : likes.size();
    }
}

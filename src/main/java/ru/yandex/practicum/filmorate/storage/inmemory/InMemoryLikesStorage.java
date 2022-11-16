package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.*;

@Component
@Qualifier(InMemoryStorageConsts.QUALIFIER)
public class InMemoryLikesStorage implements LikesStorage {
    private final Map<Integer, Set<Long>> storage = new HashMap<>();

    @Override
    public boolean contains(int filmId, long userId) {
        Set<Long> likes = storage.get(filmId);
        if (likes == null) {
            return false;
        }
        return likes.contains(userId);
    }

    @Override
    public void addLike(int filmId, long userId) {
        storage.putIfAbsent(filmId, new HashSet<>());
        storage.get(filmId).add(userId);
    }

    @Override
    public void deleteLike(int filmId, long userId) {
        Set<Long> likes = storage.get(filmId);
        if (likes == null) {
            return;
        }
        boolean result = likes.remove(userId);
        if (result && likes.isEmpty()) {
            storage.remove(filmId);
        }
    }

    @Override
    public int getLikesCount(int filmId) {
        Set<Long> likes = storage.get(filmId);
        return (likes == null) ? 0 : likes.size();
    }
}

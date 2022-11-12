package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageQualifiers;

import java.util.*;

@Component
@Qualifier(InMemoryStorageQualifiers.LIKES)
public class InMemoryLikesStorage implements LikesStorage {
    private final Map<Integer, Set<Long>> storage = new HashMap<>();

    @Override
    public boolean addLike(int filmId, long userId) {
        storage.putIfAbsent(filmId, new HashSet<>());
        return storage.get(filmId).add(userId);
    }

    @Override
    public boolean deleteLike(int filmId, long userId) {
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

    public int getLikesCount(int filmId) {
        Set<Long> likes = storage.get(filmId);
        return (likes == null) ? 0 : likes.size();
    }
}

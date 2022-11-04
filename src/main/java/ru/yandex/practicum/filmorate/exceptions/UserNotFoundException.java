package ru.yandex.practicum.filmorate.exceptions;

public class UserNotFoundException extends RuntimeException {
    private final long id;

    public UserNotFoundException(long id) {
        super("Unable to find the user #" + id);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

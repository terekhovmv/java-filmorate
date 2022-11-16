package ru.yandex.practicum.filmorate.exceptions;

public class MpaNotFoundException extends RuntimeException {
    private final short id;

    public MpaNotFoundException(short id) {
        super("Unable to find MPA #" + id);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

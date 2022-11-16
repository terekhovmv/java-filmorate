package ru.yandex.practicum.filmorate.exceptions;

public class GenreNotFoundException extends RuntimeException {
    private final short id;

    public GenreNotFoundException(short id) {
        super("Unable to find the genre #" + id);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

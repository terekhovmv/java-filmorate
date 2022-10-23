package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFoundException extends RuntimeException {
    private final int id;

    public FilmNotFoundException(int id) {
        super("Unable to find the film #" + id);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

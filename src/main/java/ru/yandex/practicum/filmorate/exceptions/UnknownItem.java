package ru.yandex.practicum.filmorate.exceptions;

public class UnknownItem extends RuntimeException {
    public UnknownItem(String message) {
        super(message);
    }
}

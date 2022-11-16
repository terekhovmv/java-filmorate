package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Value
public class Mpa {
    @NotNull
    Short id;
    String name;
}

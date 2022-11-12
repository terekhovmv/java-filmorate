package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Mpa {
    short id;
    String name;
}

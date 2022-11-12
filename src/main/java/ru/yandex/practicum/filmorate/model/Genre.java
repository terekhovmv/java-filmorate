package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Genre {
    short id;
    String name;
}

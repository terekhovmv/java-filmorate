package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import ru.yandex.practicum.filmorate.annotations.MovieEra;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @MovieEra
    private LocalDate releaseDate;
    @Positive
    private int duration;
}

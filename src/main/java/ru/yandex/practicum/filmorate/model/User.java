package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    @Past
    LocalDate birthday;
}

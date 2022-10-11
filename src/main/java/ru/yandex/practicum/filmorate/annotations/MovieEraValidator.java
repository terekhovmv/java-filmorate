package ru.yandex.practicum.filmorate.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MovieEraValidator implements ConstraintValidator<MovieEra, LocalDate> {
    private static final LocalDate MIN = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext cxt) {
        if (value == null) {
            return false;
        }

        return !value.isBefore(MIN);
    }
}

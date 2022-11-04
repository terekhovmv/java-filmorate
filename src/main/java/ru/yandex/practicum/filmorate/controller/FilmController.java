package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Film getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    public List<Film> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseBody
    public Film create(@RequestBody @Valid Film archetype) {
        return service.create(archetype);
    }

    @PutMapping
    @ResponseBody
    public Film update(@RequestBody @Valid Film patched) {
        return service.update(patched);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseBody
    public void addFilmLike(@PathVariable int id, @PathVariable long userId) {
        service.addFilmLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseBody
    public void deleteFilmLike(@PathVariable int id, @PathVariable long userId) {
        service.deleteFilmLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseBody
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") @Valid @Min(1) int count) {
        return service.getPopular(count);
    }
}

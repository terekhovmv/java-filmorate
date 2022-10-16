package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final InMemoryFilmStorage storage;

    public FilmController(InMemoryFilmStorage storage) {
        this.storage = storage;
    }

    @GetMapping
    @ResponseBody
    public List<Film> findAll() {
        return storage.findAll();
    }

    @PostMapping
    @ResponseBody
    public Film create(@RequestBody @Valid Film archetype) {
        return storage.create(archetype);
    }

    @PutMapping
    @ResponseBody
    public Film update(@RequestBody @Valid Film patched) {
        return storage.update(patched);
    }
}

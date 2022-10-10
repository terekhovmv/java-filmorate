package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage storage = new FilmStorage();

    @GetMapping
    @ResponseBody
    public List<Film> findAll() {
        return storage.findAll();
    }

    @PostMapping
    @ResponseBody
    public Film create(@RequestBody Film archetype) {
        return storage.create(archetype);
    }

    @PutMapping
    @ResponseBody
    public Film update(@RequestBody Film patched) {
        return storage.update(patched);
    }
}

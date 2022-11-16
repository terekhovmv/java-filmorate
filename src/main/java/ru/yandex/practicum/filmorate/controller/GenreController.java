package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Genre getById(@PathVariable short id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    public List<Genre> getAll() {
        return service.getAll();
    }
}

package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Mpa getById(@PathVariable short id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    public List<Mpa> getAll() {
        return service.getAll();
    }
}

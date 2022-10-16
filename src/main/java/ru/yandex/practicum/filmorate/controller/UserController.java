package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage storage;

    public UserController(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    @GetMapping
    @ResponseBody
    public List<User> findAll() {
        return storage.findAll();
    }

    @PostMapping
    @ResponseBody
    public User create(@RequestBody @Valid User archetype) {
        return storage.create(archetype);
    }

    @PutMapping
    @ResponseBody
    public User update(@RequestBody @Valid User patched) {
        return storage.update(patched);
    }
}

package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage storage = new UserStorage();

    @GetMapping
    @ResponseBody
    public List<User> findAll() {
        return storage.findAll();
    }

    @PostMapping
    @ResponseBody
    public User create(@RequestBody User archetype) {
        return storage.create(archetype);
    }

    @PutMapping
    @ResponseBody
    public User update(@RequestBody User patched) {
        return storage.update(patched);
    }
}

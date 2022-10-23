package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getById(@PathVariable long id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    public List<User> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseBody
    public User create(@RequestBody @Valid User archetype) {
        return service.create(archetype);
    }

    @PutMapping
    @ResponseBody
    public User update(@RequestBody @Valid User patched) {
        return service.update(patched);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}

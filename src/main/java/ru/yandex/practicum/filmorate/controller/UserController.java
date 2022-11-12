package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@Validated
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

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseBody
    public List<User> getFriends(@PathVariable long id) {
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseBody
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return service.getCommonFriends(id, otherId);
    }
}

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

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public void addToUserFriends(@PathVariable long id, @PathVariable long friendId) {
        service.addToUserFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public void deleteFromUserFriends(@PathVariable long id, @PathVariable long friendId) {
        service.deleteFromUserFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseBody
    public List<User> getUserFriends(@PathVariable long id) {
        return service.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseBody
    public List<User> getCommonUserFriends(@PathVariable long id, @PathVariable long otherId) {
        return service.getCommonUserFriends(id, otherId);
    }
}

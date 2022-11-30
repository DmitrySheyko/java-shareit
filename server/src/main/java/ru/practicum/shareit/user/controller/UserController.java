package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto add(@RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable(value = "id") Long userId,
                          @RequestBody UserDto userDtoForUpdate) {
        userDtoForUpdate.setId(userId);
        return userService.update(userDtoForUpdate);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable(value = "id") Long userId) {
        return userService.getById(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable(value = "id") Long userId) {
//        return userService.delete(userId);
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long userId) {
        userService.delete(userId);
    }
}
//headers = "Content-Type: application/json; charset=UTF-8")
package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.interfaces.Controllers;
import ru.practicum.shareit.interfaces.Services;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController implements Controllers<UserDto> {
    Services<UserDto> services;

    @Override
    @PostMapping
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        return services.add(userDto);
    }

    @Override
    @PatchMapping("/{id}")
    public UserDto update(@PathVariable(value = "id") Long userId, @RequestBody UserDto userDtoForUpdate) {
        userDtoForUpdate.setId(userId);
        return services.update(userDtoForUpdate);
    }

    @Override
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable(value = "id") Long userId) {
        return services.getById(userId);
    }

    @Override
    @GetMapping
    public List<UserDto> getAll() {
        return services.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") Long userId) {
        return services.delete(userId);
    }
}

package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
//@Validated
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    @PostMapping
    public UserDto add(@RequestBody @Valid User user) {
        System.out.println("controller");
        return userService.add(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable(value = "id") Long userId, @RequestBody User userForUpdate) {
        return userService.update(userId, userForUpdate);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable(value = "id") Long userId) {
        return userService.getById(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") Long userId) {
        return userService.delete(userId);
    }
}

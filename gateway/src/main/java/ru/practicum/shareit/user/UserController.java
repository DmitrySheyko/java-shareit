package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody UserRequestDto requestDto) {
        return userClient.add(requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Positive @PathVariable(value = "id") Long userId,
                                         @RequestBody UserRequestDto requestDto) {
        return userClient.update(userId, requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@Positive @PathVariable(value = "id") Long userId) {
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@Positive @PathVariable(value = "id") Long userId) {
        return userClient.delete(userId);
    }
}
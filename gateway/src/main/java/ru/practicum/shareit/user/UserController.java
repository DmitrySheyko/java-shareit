package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody UserRequestDto requestDto) {
        log.info("Добавление нового пользователя {}", requestDto);
        return userClient.add(requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Positive @PathVariable(value = "id") Long userId,
                                         @RequestBody UserRequestDto requestDto) {
        log.info("Обновление данных пользователя userId={}", userId);
        return userClient.update(userId, requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@Positive @PathVariable(value = "id") Long userId) {
        log.info("Получение данных пользователя userId={}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Получение списка пользователей");
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@Positive @PathVariable(value = "id") Long userId) {
        log.info("Удаление пользователя userId={}", userId);
        return userClient.delete(userId);
    }
}
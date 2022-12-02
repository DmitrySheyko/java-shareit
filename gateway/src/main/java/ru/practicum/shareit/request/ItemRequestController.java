package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.InputItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody InputItemRequestDto inputItemRequestDto) {
        inputItemRequestDto.setRequestor(userId);
        log.info("Создание запросов {}, пользователем userId={}", inputItemRequestDto, userId);
        return itemRequestClient.add(inputItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwn(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение списка запросов созданых пользователем userId={}", userId);
        return itemRequestClient.getAllOwn(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0",
                                                 required = false) int from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10", required = false)
                                         int size) {
        log.info("Получение всего списка запросов пользователем userId={}", userId);
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable(value = "id") Long requestId) {
        log.info("Получение запроса requestId={}", requestId);
        return itemRequestClient.getById(userId, requestId);
    }
}
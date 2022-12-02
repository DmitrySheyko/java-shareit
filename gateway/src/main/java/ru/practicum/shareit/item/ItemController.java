package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Создание объекта {} пользователем userId={}", itemRequestDto, userId);
        return itemClient.add(userId, itemRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Positive @PathVariable(value = "id") Long itemId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обновление объекта itemId={} пользователем userId={}", itemId, userId);
        return itemClient.update(userId, itemId, itemRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Positive @PathVariable("id") Long itemId) {
        log.info("Получение информации об объекте itemId={} пользователем userId={}", itemId, userId);
        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByOwner(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(value = "from", required = false,
                                                        defaultValue = "0") int from,
                                                @RequestParam(value = "size", required = false,
                                                        defaultValue = "10") int size) {
        log.info("Получение списка объектов пользователея userId={}", userId);
        return itemClient.getAllByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@PathParam("text") String text,
                                         @PositiveOrZero @RequestParam(value = "from", required = false,
                                                 defaultValue = "0") int from,
                                         @Positive @RequestParam(value = "size", required = false,
                                                 defaultValue = "10") int size) {
        log.info("Поиск по запросу {}", text);
        return itemClient.search(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@Positive @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                             @Positive @PathVariable(value = "itemId") Long itemId,
                                             @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Добавление комментария к объекту itemId={} пользователем userId={}", itemId, userId);
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }
}
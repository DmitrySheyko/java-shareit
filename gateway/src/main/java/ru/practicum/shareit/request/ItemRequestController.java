package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
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
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody InputItemRequestDto inputItemRequestDto) {
        inputItemRequestDto.setRequestor(userId);
        return itemRequestClient.add(inputItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwn(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getAllOwn(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0",
                                                 required = false) int from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10", required = false)
                                         int size) {
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable(value = "id") Long requestId) {
        return itemRequestClient.getById(userId, requestId);
    }
}
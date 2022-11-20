package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public OutputItemRequestDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody @Valid InputItemRequestDto inputItemRequestDto) {
        inputItemRequestDto.setRequestor(userId);
        return itemRequestService.add(inputItemRequestDto);
    }

    @GetMapping
    public List<OutputItemRequestDto> getAllOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllOwn(userId);
    }

    @GetMapping("/all")
    public List<OutputItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(value = "from", defaultValue = "0", required = false)
                                             @Min(0) int from,
                                             @RequestParam(value = "size", defaultValue = "10", required = false)
                                             @Min(1) int size) {
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public OutputItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable(value = "id") Long requestId) {
        return itemRequestService.getById(userId, requestId);
    }
}

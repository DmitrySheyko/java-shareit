package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Min;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setOwner(userId);
        return itemService.add(itemRequestDto);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable(value = "id") Long itemId,
                                  @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setId(itemId);
        itemRequestDto.setOwner(userId);
        return itemService.update(itemRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable("id") Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemResponseDtoForOwner> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(value = "from", required = false,
                                                                        defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(value = "size", required = false,
                                                                        defaultValue = "10") @Min(1) int size) {
        return itemService.getAllByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@PathParam("text") String text,
                                        @RequestParam(value = "from", required = false,
                                                        defaultValue = "0") @Min(0) int from,
                                        @RequestParam(value = "size", required = false,
                                                        defaultValue = "10") @Min(1) int size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                         @PathVariable(value = "itemId") Long itemId,
                                         @RequestBody String text) {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .author(userId)
                .item(itemId)
                .text(text.substring(15, (text.length() - 3)))
                .build();
        return itemService.addComment(commentRequestDto);
    }
}
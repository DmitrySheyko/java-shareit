package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

public interface ItemService {
    ItemResponseDto add(@Valid ItemRequestDto itemRequestDto);

    ItemResponseDto update(ItemRequestDto itemRequestDto);

    ResponseDto getById(Long userId, Long itemId);

    List<ItemResponseDtoForOwner> getAllByOwner(Long userId, int from, int size);

    CommentResponseDto addComment(@Valid CommentRequestDto commentRequestDto);

    List<ItemResponseDto> search(String text, int from, int size);

    String delete(Long userId);

    void checkIsItemInStorage(Long itemId);

    void checkIsItemAvailable(Long itemId);

    Item findById(Long itemId);
}

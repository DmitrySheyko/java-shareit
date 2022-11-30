package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    OutputItemRequestDto add(InputItemRequestDto inputItemRequestDto);

    List<OutputItemRequestDto> getAllOwn(Long userId);

    List<OutputItemRequestDto> getAll(Long userId, int from, int size);

    OutputItemRequestDto getById(Long userId, Long requestId);

    ItemRequest findById(Long requestId);
}

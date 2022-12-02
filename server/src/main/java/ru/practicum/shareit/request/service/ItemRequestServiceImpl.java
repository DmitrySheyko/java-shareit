package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
import ru.practicum.shareit.request.itemRequestMapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserServiceImpl userService;

    @Override
    public OutputItemRequestDto add(InputItemRequestDto inputItemRequestDto) {
        userService.checkIsObjectInStorage(inputItemRequestDto.getRequestor());
        ItemRequest itemRequest = itemRequestMapper.inputItemRequestDtoToEntity(inputItemRequestDto);
        itemRequest.setCreated(Instant.now());
        ItemRequest createdItemRequest = itemRequestRepository.save(itemRequest);
        OutputItemRequestDto outputItemRequestDto = itemRequestMapper.entityToOutputItemRequestDto(createdItemRequest);
        log.info(String.format("Запрос id=%s успешно создан", createdItemRequest.getId()));
        return outputItemRequestDto;
    }

    @Override
    public List<OutputItemRequestDto> getAllOwn(Long userId) {
        userService.checkIsObjectInStorage(userId);
        List<ItemRequest> requestsList = itemRequestRepository.findAllByRequestorOrderByCreatedDesc(userId);
        if (requestsList.isEmpty()) {
            log.info(String.format("Список заявок пользователя id=%s пуст", userId));
            return Collections.emptyList();
        }
        List<OutputItemRequestDto> result = requestsList.stream()
                .map(itemRequestMapper::entityToOutputItemRequestDto)
                .collect(Collectors.toList());
        log.info(String.format("Список заявок пользователя id=%s успешно получен", userId));
        return result;
    }

    @Override
    public List<OutputItemRequestDto> getAll(Long userId, int from, int size) {
        userService.checkIsObjectInStorage(userId);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<ItemRequest> requestsPage = itemRequestRepository.findAllByRequestorNot(pageable, userId);
        List<OutputItemRequestDto> result = requestsPage.stream()
                .map(itemRequestMapper::entityToOutputItemRequestDto)
                .collect(Collectors.toList());
        log.info(String.format("Список заявок page=%s, size=%s успешно получен", from, size));
        return result;
    }

    @Override
    public OutputItemRequestDto getById(Long userId, Long requestId) {
        userService.checkIsObjectInStorage(userId);
        checkIsObjectInStorage(requestId);
        ItemRequest itemRequest = findById(requestId);
        OutputItemRequestDto result = itemRequestMapper.entityToOutputItemRequestDto(itemRequest);
        log.info(String.format("Заявка Id=%s успешно получена", requestId));
        return result;
    }

    @Override
    public ItemRequest findById(Long requestId) {
        Optional<ItemRequest> optionalRequest = itemRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            return optionalRequest.get();
        } else {
            log.warn(String.format("Не удалось получить данные по заявке Id=%s", requestId));
            throw new ObjectNotFoundException(String.format("Не удалось получить данные по заявке  Id=%s", requestId));
        }
    }

    private void checkIsObjectInStorage(Long requestId) {
        if (!(requestId != null && itemRequestRepository.existsById(requestId))) {
            log.warn(String.format("Заявка Id=%s не найдена", requestId));
            throw new ObjectNotFoundException(String.format("Заявка Id=%s не найдена", requestId));
        }
    }
}

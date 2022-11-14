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
import ru.practicum.shareit.request.itemRequestMapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
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
public class ItemRequestServiceImpl {
    ItemRequestRepository itemRequestRepository;
    ItemRequestMapper itemRequestMapper;
    UserServiceImpl userService;

    public OutputItemRequestDto add(InputItemRequestDto inputItemRequestDto) {
        userService.checkIsUserInStorage(inputItemRequestDto.getRequestor());
        System.out.println("1");
        ItemRequest itemRequest = itemRequestMapper.inputItemRequestDtoToEntity(inputItemRequestDto);
        System.out.println("2");
        itemRequest.setCreated(Instant.now());
        System.out.println("3");
        ItemRequest createdItemRequest = itemRequestRepository.save(itemRequest);
        System.out.println("4");
        System.out.println(createdItemRequest);
        OutputItemRequestDto outputItemRequestDto = itemRequestMapper.entityToOutputItemRequestDto(createdItemRequest);
        System.out.println("5");
        log.info(String.format("Запрос id=%s успешно создан", createdItemRequest.getId()));
        return outputItemRequestDto;
    }

    public List<OutputItemRequestDto> getAllOwn(Long userId) {
        userService.checkIsUserInStorage(userId);
        List<ItemRequest> requestsList = itemRequestRepository.findAllByRequestorOrderByCreatedDesc(userId);
        if (requestsList.isEmpty()) {
            log.info(String.format("Список заявок пользователя id=%s пуст", userId));
            return Collections.emptyList();
        } else {
            List<OutputItemRequestDto> result = requestsList.stream()
                    .map(itemRequest -> itemRequestMapper.entityToOutputItemRequestDto(itemRequest))
                    .collect(Collectors.toList());
            log.info(String.format("Список заявок пользователя id=%s успешно получен", userId));
            return result;
        }
    }

    public List<OutputItemRequestDto> getAll(Long userId, int from, int size) {
        userService.checkIsUserInStorage(userId);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<ItemRequest> requestsPage = itemRequestRepository.findAllByRequestorNot(pageable, userId);
        List<OutputItemRequestDto> result = requestsPage.stream()
                .map(itemRequest -> itemRequestMapper.entityToOutputItemRequestDto(itemRequest))
                .collect(Collectors.toList());
        log.info(String.format("Список заявок page=%s, size=%s успешно получен", from, size));
        return result;
    }

    public OutputItemRequestDto getById(Long userId, Long requestId) {
        userService.checkIsUserInStorage(userId);
        checkIsObjectInStorage(requestId);
        ItemRequest itemRequest = findById(requestId);
        OutputItemRequestDto result = itemRequestMapper.entityToOutputItemRequestDto(itemRequest);
        log.info(String.format("Заявка Id=%s успешно получена", requestId));
        return result;
    }

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
        if (!itemRequestRepository.existsById(requestId)) {
            log.warn(String.format("Заявка Id=%s не найдена", requestId));
            throw new ObjectNotFoundException(String.format("Заявка Id=%s не найдена", requestId));
        }
    }
}
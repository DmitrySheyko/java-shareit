package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestService;
    private String description = "Request description";
    private final Long requestor = 2L;
    private final Long requestId = 5L;

    @Test
    void add() {
        InputItemRequestDto inputItemRequestDto = InputItemRequestDto.builder().description(description)
                .requestor(requestor).build();
        OutputItemRequestDto outputItemRequestDto = itemRequestService.add(inputItemRequestDto);
        Assertions.assertNotNull(outputItemRequestDto.getId());
        Assertions.assertEquals(description, outputItemRequestDto.getDescription());
    }

    @Test
    void getAllOwn() {
        int numberOfRequests = 2;
        List<OutputItemRequestDto> result = itemRequestService.getAllOwn(requestor);
        Assertions.assertEquals(numberOfRequests, result.size());

        Long notExistsRequestor = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getAllOwn(notExistsRequestor));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsRequestor),
                thrown.getMessage());
    }

    @Test
    void getAll() {
        int numberOfRequests = 3;
        int from = 0;
        int size = 10;
        List<OutputItemRequestDto> result = itemRequestService.getAll(requestor, from, size);
        Assertions.assertEquals(numberOfRequests, result.size());

        size = 1;
        result = itemRequestService.getAll(requestor, from, size);
        Assertions.assertEquals(size, result.size());

        from = 1;
        result = itemRequestService.getAll(requestor, from, size);
        Assertions.assertEquals(2, result.get(0).getId());

        Long notExistsRequestor = 100L;
        int from1 = 0;
        int size1 = 10;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getAll(notExistsRequestor,
                        from1, size1));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsRequestor),
                thrown.getMessage());
    }

    @Test
    void getById() {
        description = "Request5 description";
        OutputItemRequestDto outputItemRequestDto = itemRequestService.getById(requestor, requestId);
        Assertions.assertEquals(requestId, outputItemRequestDto.getId());
        Assertions.assertEquals(description, outputItemRequestDto.getDescription());

        Long notExistsRequestor = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(notExistsRequestor, requestId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsRequestor),
                thrown.getMessage());

        Long nullRequestor = 100L;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(nullRequestor, requestId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullRequestor), thrown.getMessage());

        Long notExistsRequestId = 100L;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(requestor, notExistsRequestId));
        Assertions.assertEquals(String.format("Заявка Id=%s не найдена", notExistsRequestId), thrown.getMessage());

        Long nullRequestId = 100L;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(requestor, nullRequestId));
        Assertions.assertEquals(String.format("Заявка Id=%s не найдена", nullRequestId), thrown.getMessage());
    }

    @Test
    void findById() {
        description = "Request5 description";
        ItemRequest itemRequest = itemRequestService.findById(requestId);
        Assertions.assertEquals(requestId, itemRequest.getId());
        Assertions.assertEquals(description, itemRequest.getDescription());

        Long notExistsRequestId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.findById(notExistsRequestId));
        Assertions.assertEquals(String.format("Не удалось получить данные по заявке  Id=%S", notExistsRequestId),
                thrown.getMessage());

        Long nullRequestId = 100L;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.findById(nullRequestId));
        Assertions.assertEquals(String.format("Не удалось получить данные по заявке  Id=%S", nullRequestId),
                thrown.getMessage());
    }
}
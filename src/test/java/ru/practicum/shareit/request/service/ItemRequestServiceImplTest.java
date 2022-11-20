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
import ru.practicum.shareit.request.itemRequestMapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    final ItemRequestServiceImpl itemRequestService;
    final ItemRequestRepository itemRequestRepository;
    final ItemRequestMapper itemRequestMapper;
    final UserServiceImpl userService;
    String description = "Request description";
    Long requestor = 2L;
    Long requestId = 5L;

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
        Assertions.assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getAllOwn(notExistsRequestor));
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
        Assertions.assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getAll(notExistsRequestor,
                from1, size1));
    }

    @Test
    void getById() {
        description = "Request5 description";
        OutputItemRequestDto outputItemRequestDto = itemRequestService.getById(requestor, requestId);
        Assertions.assertEquals(requestId, outputItemRequestDto.getId());
        Assertions.assertEquals(description, outputItemRequestDto.getDescription());

        Long notExistsRequestor = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(notExistsRequestor, requestId));

        Long nullRequestor = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(nullRequestor, requestId));

        Long notExistsRequestId = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(requestor, notExistsRequestId));

        Long nullRequestId = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getById(requestor, nullRequestId));
    }

    @Test
    void findById() {
        description = "Request5 description";
        ItemRequest itemRequest = itemRequestService.findById(requestId);
        Assertions.assertEquals(requestId, itemRequest.getId());
        Assertions.assertEquals(description, itemRequest.getDescription());

        Long notExistsRequestId = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.findById(notExistsRequestId));

        Long nullRequestId = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.findById(nullRequestId));
    }
}
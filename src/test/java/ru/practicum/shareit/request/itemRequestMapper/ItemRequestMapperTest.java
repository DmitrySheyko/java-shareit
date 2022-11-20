package ru.practicum.shareit.request.itemRequestMapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestMapperTest {
    final ItemRequestMapper itemRequestMapper;
    final ItemRequestService itemRequestService;

    @Test
    void inputItemRequestDtoToEntity() {
        InputItemRequestDto inputItemRequestDto = InputItemRequestDto.builder().requestor(1L).description("Description")
                .build();

        ItemRequest itemRequest = itemRequestMapper.inputItemRequestDtoToEntity(inputItemRequestDto);
        Assertions.assertEquals(itemRequest.getRequestor(), 1L);
        Assertions.assertEquals(itemRequest.getDescription(), "Description");

        inputItemRequestDto = null;
        itemRequest = itemRequestMapper.inputItemRequestDtoToEntity(inputItemRequestDto);
        Assertions.assertNull(itemRequest);
    }

    @Test
    void entityToOutputItemRequestDto() {
        Long requestId = 1L;
        String description = "Request1 description";
        String created = "2022-01-01T10:10:10";
        int createdItems = 2;
        ItemRequest itemRequest = itemRequestService.findById(requestId);
        OutputItemRequestDto outputItemRequestDto = itemRequestMapper.entityToOutputItemRequestDto(itemRequest);
        Assertions.assertEquals(requestId, outputItemRequestDto.getId());
        Assertions.assertEquals(description, outputItemRequestDto.getDescription());
        Assertions.assertEquals(createdItems, outputItemRequestDto.getItems().size());
        Assertions.assertEquals(created, outputItemRequestDto.getCreated());

        requestId = 4L;
        description = "Request4 description";
        created = "2022-04-01T10:10:10";
        createdItems = 0;
        itemRequest = itemRequestService.findById(requestId);
        outputItemRequestDto = itemRequestMapper.entityToOutputItemRequestDto(itemRequest);
        Assertions.assertEquals(requestId, outputItemRequestDto.getId());
        Assertions.assertEquals(description, outputItemRequestDto.getDescription());
        Assertions.assertEquals(createdItems, outputItemRequestDto.getItems().size());
        Assertions.assertEquals(created, outputItemRequestDto.getCreated());
    }
}
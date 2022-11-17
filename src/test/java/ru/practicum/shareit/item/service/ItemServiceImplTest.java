package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    final ItemServiceImpl itemService;
    final ItemRepository itemRepository;
    final CommentRepository commentRepository;
    final BookingRepository bookingRepository;
    final ItemMapper itemMapper;
    final CommentMapper commentMapper;
    final UserServiceImpl userServiceImpl;

    @Mock
    BookingRepository mockBookingRepository;

    @Test
    void add() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name("TestItem").description("Test description")
                .owner(1L).available(true).build();
        ItemResponseDto itemResponseDto = itemService.add(itemRequestDto);
        Assertions.assertNotNull(itemResponseDto.getId());
        Assertions.assertEquals(itemResponseDto.getName(), itemResponseDto.getName());
        Assertions.assertEquals(itemResponseDto.getDescription(), itemResponseDto.getDescription());
        Assertions.assertTrue(itemResponseDto.getAvailable());

        ItemRequestDto requestWithoutName = ItemRequestDto.builder().description("Test description")
                .owner(1L).available(true).build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> itemService.add(requestWithoutName));
    }

    @Test
    void update() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name("TestItem").description("Test description")
                .owner(1L).available(true).build();
        ItemResponseDto newItemResponseDto = itemService.add(itemRequestDto);
        ItemRequestDto itemDtoForUpdate = ItemRequestDto.builder().id(newItemResponseDto.getId()).name("Updated name")
                .description("Updated description").available(false).owner(1L).build();
        ItemResponseDto updatedItemResponseDto = itemService.update(itemDtoForUpdate);
        Assertions.assertEquals(itemDtoForUpdate.getId(), updatedItemResponseDto.getId());
        Assertions.assertEquals(itemDtoForUpdate.getName(), updatedItemResponseDto.getName());
        Assertions.assertEquals(itemDtoForUpdate.getDescription(), updatedItemResponseDto.getDescription());
        Assertions.assertEquals(itemDtoForUpdate.getAvailable(), updatedItemResponseDto.getAvailable());

        ItemRequestDto itemRequestWithIncorrectOwner = ItemRequestDto.builder().name("TestItem").description("Test description")
                .owner(100L).available(true).build();
        Assertions.assertThrows(ObjectNotFoundException.class, () -> itemService.update(itemRequestWithIncorrectOwner));
        ItemRequestDto itemRequestWithIncorrectItem = ItemRequestDto.builder().id(100L).name("TestItem").description("Test description")
                .owner(100L).available(true).build();
        Assertions.assertThrows(ObjectNotFoundException.class, () -> itemService.update(itemRequestWithIncorrectItem));
        itemDtoForUpdate = ItemRequestDto.builder().id(newItemResponseDto.getId()).owner(1L).available(true).build();
        itemService.update(itemDtoForUpdate);
    }

    @Test
    void getById() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name("ItemName").description("Description")
                .owner(1L).available(true).build();
        ItemResponseDto itemResponseDto = itemService.add(itemRequestDto);
        Long itemId = itemResponseDto.getId();
        ItemResponseDtoForOwner itemFromBd = (ItemResponseDtoForOwner) itemService.getById(1L, itemId);
        Assertions.assertEquals(itemId, itemFromBd.getId());
        Assertions.assertEquals(itemRequestDto.getName(), itemFromBd.getName());
        Assertions.assertEquals(itemRequestDto.getDescription(), itemFromBd.getDescription());
        Assertions.assertEquals(itemRequestDto.getAvailable(), itemFromBd.getAvailable());
    }

    @Test
    void getAllByOwner() {
        List<ItemResponseDtoForOwner> result = itemService.getAllByOwner(1L, 0, 10);
        Assertions.assertEquals(result.size(), 2);
    }

    @Test
    void addComment() {
        itemService.setBookingRepository(mockBookingRepository);
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text("Test text").item(1L).author(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .item(itemService.findById(1L))
                .booker(userServiceImpl.findById(2L))
                .start(Instant.parse("2020-09-10T10:10:10.00Z"))
                .end(Instant.parse("2020-10-10T10:10:10.00Z"))
                .build();

        Mockito.when(mockBookingRepository.findAllByBookerIdAndItemIdAndEndIsBefore(Mockito.anyLong(),
                        Mockito.anyLong(), Mockito.any(Instant.class)))
                .thenReturn(List.of(booking));

        CommentResponseDto commentFromDB = itemService.addComment(commentForAdd);
        Assertions.assertEquals("Test text", commentFromDB.getText());
        Assertions.assertNotNull(commentFromDB.getId());
        Assertions.assertNotNull(commentFromDB.getCreated());

        CommentRequestDto commentWithEmptyText = CommentRequestDto.builder().text(" ").item(1L).author(2L).build();
        Assertions.assertThrows(ValidationException.class, () -> itemService.addComment(commentWithEmptyText));
    }

    @Test
    void search() {
        ItemRequestDto itemRequestDto1 = ItemRequestDto.builder().name("TestItemForSEARCH").description("Test description")
                .owner(1L).available(true).build();
        ItemRequestDto itemRequestDto2 = ItemRequestDto.builder().name("TestItem").description("Test description for search")
                .owner(1L).available(true).build();
        itemService.add(itemRequestDto1);
        itemService.add(itemRequestDto2);
        List<ItemResponseDto> result = itemService.search("earc", 0, 10);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void delete() {
        List<Item> listOfItems = itemRepository.findAll();
        int lengthOfList = listOfItems.size();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name("TestItem").description("Test description")
                .owner(1L).available(true).build();
        ItemResponseDto itemResponseDto = itemService.add(itemRequestDto);
        List<Item> listAfterAddItem = itemRepository.findAll();
        itemService.delete(itemResponseDto.getId());
        List<Item> listAfterDeleteItem = itemRepository.findAll();
        Assertions.assertEquals(lengthOfList + 1, listAfterAddItem.size());
        Assertions.assertEquals(lengthOfList, listAfterDeleteItem.size());
    }

    @Test
    void checkIsItemInStorage() {
        Assertions.assertThrows(ObjectNotFoundException.class, () -> itemService.checkIsItemInStorage(100L));
        Assertions.assertDoesNotThrow(() -> itemService.checkIsItemInStorage(1L));
    }

    @Test
    void checkIsItemAvailable() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name("TestItem").description("Test description")
                .owner(1L).available(true).build();
        ItemResponseDto itemResponseDto = itemService.add(itemRequestDto);
        Assertions.assertDoesNotThrow(() -> itemService.checkIsItemAvailable(itemResponseDto.getId()));
        itemRequestDto.setId(itemResponseDto.getId());
        itemRequestDto.setAvailable(false);
        ItemResponseDto updatedItemResponseDto = itemService.update(itemRequestDto);
        Assertions.assertThrows(ValidationException.class,
                () -> itemService.checkIsItemAvailable(updatedItemResponseDto.getId()));
    }

    @Test
    void findById() {
        Item result = itemService.findById(1L);
        Assertions.assertEquals("Item1", result.getName());
        Assertions.assertEquals("Description1", result.getDescription());
        Assertions.assertEquals(1L, result.getOwner());
    }
}
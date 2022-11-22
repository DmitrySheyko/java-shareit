package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final ItemServiceImpl itemService;
    private final ItemRepository itemRepository;

    @Test
    void add() {
        String name = "TestItem";
        String description = "Test description";
        Long owner = 1L;
        Boolean available = true;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name(name).description(description)
                .owner(owner).available(available).build();
        ItemResponseDto itemResponseDto = itemService.add(itemRequestDto);
        Assertions.assertNotNull(itemResponseDto.getId());
        Assertions.assertEquals(name, itemResponseDto.getName());
        Assertions.assertEquals(description, itemResponseDto.getDescription());
        Assertions.assertTrue(available);

        Long notExistsUserId = 100L;
        ItemRequestDto itemRequestDtoWithNotExistUserId = ItemRequestDto.builder().name(name).description(description)
                .owner(notExistsUserId).available(available).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.add(itemRequestDtoWithNotExistUserId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsUserId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveIncorrectName() {
        ItemRequestDto itemRequestDtoWithNullName = ItemRequestDto.builder().description("Test description").owner(1L)
                .available(true).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDtoWithNullName));
        Assertions.assertEquals("add.itemRequestDto.name: Не указано название", thrown.getMessage());

        ItemRequestDto itemRequestDtoWithEmptyName = ItemRequestDto.builder().name("").description("Test description")
                .owner(1L).available(true).build();
        thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDtoWithEmptyName));
        Assertions.assertEquals("add.itemRequestDto.name: Не указано название", thrown.getMessage());

        ItemRequestDto itemRequestDtoWithBlankName = ItemRequestDto.builder().name(" ").description("Test description")
                .owner(1L).available(true).build();
        thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDtoWithBlankName));
        Assertions.assertEquals("add.itemRequestDto.name: Не указано название", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveIncorrectDescription() {
        ItemRequestDto itemRequestDtoWithNullDescription = ItemRequestDto.builder().name("TestItem").owner(1L)
                .available(true).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDtoWithNullDescription));
        Assertions.assertEquals("add.itemRequestDto.description: Не указано описание", thrown.getMessage());

        ItemRequestDto itemRequestDtoWithEmptyDescription = ItemRequestDto.builder().name("TestItem").description("")
                .owner(1L).available(true).build();
        thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDtoWithEmptyDescription));
        Assertions.assertEquals("add.itemRequestDto.description: Не указано описание", thrown.getMessage());

        ItemRequestDto itemRequestDtoWithBlankDescription = ItemRequestDto.builder().name("TestItem").description(" ")
                .owner(1L).available(true).build();
        thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDtoWithBlankDescription));
        Assertions.assertEquals("add.itemRequestDto.description: Не указано описание",
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveNullAvailable() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name("TestItem").description("Test description")
                .owner(1L).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDto));
        Assertions.assertEquals("add.itemRequestDto.available: must not be null", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveNullOwner() {
        ItemRequestDto itemRequestDtoWithNullOwner = ItemRequestDto.builder().name("TestItem")
                .description("Test description").available(true).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.add(itemRequestDtoWithNullOwner));
        Assertions.assertEquals("add.itemRequestDto.owner: В запросе не предостален id пользователя",
                thrown.getMessage());
    }

    @Test
    void update() {
        String name = "TestItem";
        String description = "Test description";
        Long owner = 1L;
        Boolean available = true;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().name(name).description(description).owner(owner)
                .available(available).build();
        ItemResponseDto newItemResponseDto = itemService.add(itemRequestDto);
        Long itemId = newItemResponseDto.getId();
        String nameForUpdate = "UpdatedItem";
        String descriptionForUpdate = "Updated description";
        Boolean availableForUpdate = false;
        ItemRequestDto itemDtoForUpdate = ItemRequestDto.builder().id(itemId).name(nameForUpdate)
                .description(descriptionForUpdate).available(availableForUpdate).owner(owner).build();
        ItemResponseDto updatedItemResponseDto = itemService.update(itemDtoForUpdate);
        Assertions.assertEquals(itemId, updatedItemResponseDto.getId());
        Assertions.assertEquals(nameForUpdate, updatedItemResponseDto.getName());
        Assertions.assertEquals(descriptionForUpdate, updatedItemResponseDto.getDescription());
        Assertions.assertEquals(availableForUpdate, updatedItemResponseDto.getAvailable());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNotOwner() {
        Long itemId = 1L;
        Long owner = 2L;
        String name = "TestItem";
        String description = "Test description";
        Boolean available = true;
        ItemRequestDto itemDtoForUpdate = ItemRequestDto.builder().id(itemId).name(name)
                .description(description).available(available).owner(owner).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.update(itemDtoForUpdate));
        Assertions.assertEquals(String.format("У пользователя userId=%s нет прав редактировать объект itemId=%s", owner,
                itemId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNullOwner() {
        Long itemId = 1L;
        Long owner = null;
        String name = "TestItem";
        String description = "Test description";
        Boolean available = true;
        ItemRequestDto itemDtoForUpdate = ItemRequestDto.builder().id(itemId).name(name)
                .description(description).available(available).owner(owner).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.update(itemDtoForUpdate));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", owner), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNullItemId() {
        Long itemId = null;
        Long owner = 1L;
        String name = "TestItem";
        String description = "Test description";
        Boolean available = true;
        ItemRequestDto itemDtoForUpdate = ItemRequestDto.builder().id(itemId).name(name)
                .description(description).available(available).owner(owner).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.update(itemDtoForUpdate));
        Assertions.assertEquals(String.format("Объект itemId=%s не найден", itemId), thrown.getMessage());
    }

    @Test
    void getById() {
        Long itemId = 1L;
        Long ownerId = 1L;
        Long anotherUserId = 2L;
        ResponseDto itemResponseDtoForOwner = itemService.getById(ownerId, itemId);
        ResponseDto itemResponseDto = itemService.getById(anotherUserId, itemId);
        Assertions.assertNotNull(((ItemResponseDtoForOwner) itemResponseDtoForOwner).getLastBooking());
        Assertions.assertNotNull(((ItemResponseDtoForOwner) itemResponseDtoForOwner).getNextBooking());
        Assertions.assertNull(((ItemResponseDto) itemResponseDto).getLastBooking());
        Assertions.assertNull(((ItemResponseDto) itemResponseDto).getNextBooking());

        Long notExistsItemId = 100L;
        Long notExistsUserId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.getById(ownerId, notExistsUserId));
        Assertions.assertEquals(String.format("Объект itemId=%s не найден", notExistsUserId), thrown.getMessage());
        thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.getById(notExistsItemId, itemId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsItemId), thrown.getMessage());
    }

    @Test
    void getAllByOwner() {
        Long ownerId = 1L;
        int numberOfItems = 2;
        int pageSize = 10;
        List<ItemResponseDtoForOwner> result = itemService.getAllByOwner(ownerId, 0, pageSize);
        Assertions.assertEquals(numberOfItems, result.size());

        Long firstItemId = 1L;
        Long secondItemId = 2L;
        pageSize = 1;
        int firsPageNumber = 0;
        int secondPageNumber = 1;
        result = itemService.getAllByOwner(ownerId, firsPageNumber, pageSize);
        Assertions.assertEquals(pageSize, result.size());
        Assertions.assertEquals(firstItemId, result.get(0).getId());
        result = itemService.getAllByOwner(ownerId, secondPageNumber, pageSize);
        Assertions.assertEquals(secondItemId, result.get(0).getId());

        int size = 1;
        Long notExistsOwnerId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.getAllByOwner(notExistsOwnerId, firsPageNumber, size));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsOwnerId), thrown.getMessage());
    }

    @Test
    void addComment() {
        Long authorId = 4L;
        String authorName = "User4";
        Long itemId = 2L;

        CommentRequestDto commentForAdd = CommentRequestDto.builder().text("Text of comment").item(itemId)
                .author(authorId).build();
        CommentResponseDto commentFromDB = itemService.addComment(commentForAdd);
        Assertions.assertNull(commentForAdd.getId());
        Assertions.assertNotNull(commentFromDB.getId());
        Assertions.assertNotNull(commentFromDB.getCreated());
        Assertions.assertEquals(commentForAdd.getText(), commentFromDB.getText());
        Assertions.assertEquals(authorName, commentFromDB.getAuthorName());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithAuthorWhoNotBookedItemBefore() {
        Long authorId = 3L;
        Long itemId = 3L;
        String textOfComment = "Text of comment";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals(String.format("Пользователь userId=%s не может оставить комментарий", authorId),
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWhenAuthorIsOwnerOfItem() {
        Long authorId = 2L;
        Long itemId = 3L;
        String textOfComment = "Text of comment";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals(String.format("Пользователь userId=%s не может оставить комментарий", authorId),
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithNotExistsAuthorId() {
        Long authorId = 100L;
        Long itemId = 3L;
        String textOfComment = "Text of comment";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", authorId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithNullAuthorId() {
        Long authorId = null;
        Long itemId = 3L;
        String textOfComment = "Text of comment";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals("addComment.commentRequestDto.author: В запросе не предостален id " +
                "пользователя", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithNotExistsItemId() {
        Long authorId = 1L;
        Long itemId = 100L;
        String textOfComment = "Text of comment";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals(String.format("Объект itemId=%s не найден", itemId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithNullItemId() {
        Long authorId = 1L;
        Long itemId = null;
        String textOfComment = "Text of comment";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals("addComment.commentRequestDto.item: В запросе не предостален id объекта",
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithNullText() {
        Long authorId = 1L;
        Long itemId = 3L;
        String textOfComment = null;
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals("addComment.commentRequestDto.text: Текст комментария не должен быть пустым",
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithEmptyText() {
        Long authorId = 1L;
        Long itemId = 3L;
        String textOfComment = "";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals("addComment.commentRequestDto.text: Текст комментария не должен быть пустым",
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentWithBlankText() {
        Long authorId = 1L;
        Long itemId = 3L;
        String textOfComment = " ";
        CommentRequestDto commentForAdd = CommentRequestDto.builder().text(textOfComment).item(itemId)
                .author(authorId).build();
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class,
                () -> itemService.addComment(commentForAdd));
        Assertions.assertEquals("addComment.commentRequestDto.text: Текст комментария не должен быть пустым",
                thrown.getMessage());
    }

    @Test
    void search() {
        String textForSearch = "search";
        int pageSize = 10;
        int firsPageNumber = 0;
        int numberOfItemsSuitableForSearch = 3;
        List<ItemResponseDto> result = itemService.search(textForSearch, firsPageNumber, pageSize);
        Assertions.assertEquals(numberOfItemsSuitableForSearch, result.size());

        String textForSearchWithDifferentCase = "SEArch";
        result = itemService.search(textForSearchWithDifferentCase, firsPageNumber, pageSize);
        Assertions.assertEquals(3, result.size());

        pageSize = 1;
        int secondPageNumber = 1;
        result = itemService.search(textForSearch, secondPageNumber, pageSize);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListIfTextForSearchIsNull() {
        String textForSearch = null;
        List<ItemResponseDto> result = itemService.search(textForSearch, 0, 10);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListIfTextForSearchIsEmpty() {
        String textForSearch = "";
        List<ItemResponseDto> result = itemService.search(textForSearch, 0, 10);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListIfTextForSearchIsBlank() {
        String textForSearch = " ";
        List<ItemResponseDto> result = itemService.search(textForSearch, 0, 10);
        Assertions.assertTrue(result.isEmpty());
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
        Long existsItemId = 1L;
        Long notExistsItemId = 100L;
        Assertions.assertDoesNotThrow(() -> itemService.checkIsItemInStorage(existsItemId));
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.checkIsItemInStorage(notExistsItemId));
        Assertions.assertEquals(String.format("Объект itemId=%s не найден", notExistsItemId), thrown.getMessage());
    }

    @Test
    void checkIsItemAvailable() {
        Long availableItemId = 1L;
        Long unAvailableItemId = 2L;
        Assertions.assertDoesNotThrow(() -> itemService.checkIsItemAvailable(availableItemId));
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> itemService.checkIsItemAvailable(unAvailableItemId));
        Assertions.assertEquals(String.format("Объект itemId=%s не доступен", unAvailableItemId), thrown.getMessage());
    }

    @Test
    void findById() {
        Long existsItemId = 1L;
        Item result = itemService.findById(existsItemId);
        Assertions.assertEquals("Item1", result.getName());
        Assertions.assertEquals("Description1", result.getDescription());
        Assertions.assertEquals(1L, result.getOwner());

        Long notExistsItemId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.findById(notExistsItemId));
        Assertions.assertEquals(String.format("Информция об объекте itemId=%s не найдена", notExistsItemId),
                thrown.getMessage());
    }
}
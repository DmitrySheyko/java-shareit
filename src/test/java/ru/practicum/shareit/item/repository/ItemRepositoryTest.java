package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;

import javax.validation.ConstraintViolationException;

@DataJpaTest(properties = "spring.sql.init.data-locations=data-test.sql")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldAddIdWhenSaveNewEntity() {
        Item item = Item.builder().name("ItemName").description("Item description").owner(1L).available(true).build();
        Assertions.assertNull(item.getId());
        itemRepository.save(item);
        Assertions.assertNotNull(item.getId());
    }

    @Test
    void shouldThrowExceptionWhenSaveNullName() {
        Item item = Item.builder().description("Item description").owner(1L).available(true).build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullDescription() {
        Item item = Item.builder().name("ItemName").owner(1L).available(true).build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullAvailable() {
        Item item = Item.builder().name("ItemName").description("Item description").owner(1L).build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullOwner() {
        Item item = Item.builder().name("ItemName").description("Item description").available(true).build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    void shouldFindAllByOwner() {
        Long owner = 2L;
        int numberOfItemsOfUser = 2;
        Long firstIdOfItemOfOwner = 3L;
        Long secondIdOfItemOfOwner = 4L;
        Page<Item> oneItemPage = itemRepository.findAllByOwner(owner, PageRequest.of(0, 1, Sort.by("id")));
        Page<Item> tenItemPage = itemRepository.findAllByOwner(owner, PageRequest.of(0, 10, Sort.by("id")));
        Assertions.assertEquals(numberOfItemsOfUser, oneItemPage.getTotalElements());
        Assertions.assertEquals(1, oneItemPage.getNumberOfElements());
        Assertions.assertEquals(2, tenItemPage.getNumberOfElements());
        Assertions.assertEquals(firstIdOfItemOfOwner, tenItemPage.toList().get(0).getId());
        Assertions.assertEquals(secondIdOfItemOfOwner, tenItemPage.toList().get(1).getId());
    }

    @Test
    void search() {
        String text = "search";
        int numberIfItemSuitsForSearch = 3;
        Page<Item> oneItemPage = itemRepository.search(text, PageRequest.of(0, 1, Sort.by("id")));
        Page<Item> tenItemPage = itemRepository.search(text, PageRequest.of(0, 10, Sort.by("id")));
        Assertions.assertEquals(numberIfItemSuitsForSearch, oneItemPage.getTotalElements());
        Assertions.assertEquals(1, oneItemPage.getNumberOfElements());
        Assertions.assertEquals(3, tenItemPage.getNumberOfElements());
    }
}
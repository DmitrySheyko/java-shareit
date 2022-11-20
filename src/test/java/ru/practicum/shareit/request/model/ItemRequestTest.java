package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

class ItemRequestTest {
    Item item = Item.builder().id(1L).name("Name 1").description("Description 1").owner(1L).available(true)
            .requestId(1L).build();
    ItemRequest itemRequest1 = ItemRequest.builder().id(1L).description("Description").requestor(2L)
            .created(Instant.now()).answersList(List.of(item)).build();
    ItemRequest itemRequest2 = new ItemRequest(2L, "Description", 2L, Instant.now(),
            Collections.emptyList());
    ItemRequest itemRequest3 = itemRequest1;

    @Test
    void testEquals() {
        Assertions.assertEquals(itemRequest1, itemRequest3);
        Assertions.assertNotEquals(itemRequest1, itemRequest2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(itemRequest1.hashCode(), itemRequest3.hashCode());
        Assertions.assertNotEquals(itemRequest1.hashCode(), itemRequest2.hashCode());
    }
}
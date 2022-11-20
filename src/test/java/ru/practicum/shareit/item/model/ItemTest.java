package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ItemTest {
    Item item1 = Item.builder().id(1L).name("Name 1").description("Description 1").owner(1L).available(true)
            .requestId(1L).build();
    Item item2 = Item.builder().id(2L).name("Name 1").description("Description 1").owner(1L).available(true)
            .requestId(1L).build();
    Item item3 = item1;

    @Test
    void testEquals() {
        Assertions.assertEquals(item1, item3);
        Assertions.assertNotEquals(item1, item2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(item1.hashCode(), item3.hashCode());
        Assertions.assertNotEquals(item1.hashCode(), item2.hashCode());
    }
}
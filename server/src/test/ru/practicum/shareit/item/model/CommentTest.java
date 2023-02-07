package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class CommentTest {
    private final Comment comment1 = Comment.builder().id(1L).text("Text").author(2L).created(Instant.now()).item(3L)
            .build();
    private final Comment comment2 = new Comment(2L, "Text", 3L, 2L, Instant.now());
    private final Comment comment3 = comment1;

    @Test
    void testEquals() {
        Assertions.assertEquals(comment1, comment3);
        Assertions.assertNotEquals(comment1, comment2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(comment1.hashCode(), comment3.hashCode());
        Assertions.assertNotEquals(comment1.hashCode(), comment2.hashCode());
    }
}
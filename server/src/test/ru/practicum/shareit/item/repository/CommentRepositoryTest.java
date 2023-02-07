package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.item.model.Comment;

import java.time.Instant;
import java.util.List;

@DataJpaTest(properties = "spring.sql.init.data-locations=data-test.sql")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldAddIdWhenSaveNewEntity() {
        Comment comment = Comment.builder().text("Comment text").item(1L).author(2L).created(Instant.now()).build();
        Assertions.assertNull(comment.getId());
        commentRepository.save(comment);
        Assertions.assertNotNull(comment.getId());
    }

    @Test
    void shouldThrowExceptionWhenSaveNullText() {
        Comment comment = Comment.builder().item(1L).author(2L).created(Instant.now()).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullItem() {
        Comment comment = Comment.builder().text("Comment text").author(2L).created(Instant.now()).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullCreated() {
        Comment comment = Comment.builder().text("Comment text").item(1L).author(2L).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    void findByItem() {
        Long itemId = 1L;
        int numberOfComments = 2;
        List<Comment> result = commentRepository.findByItem(itemId);
        Assertions.assertEquals(numberOfComments, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(2, result.get(1).getId());
    }
}
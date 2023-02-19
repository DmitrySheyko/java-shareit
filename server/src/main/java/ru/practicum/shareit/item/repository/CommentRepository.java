package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Interface of JpaRepository for entity {@link Comment}.
 *
 * @author DmitrySheyko
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItem(Long itemId);

}

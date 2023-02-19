package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Comment;

/**
 * Class of dto for entity {@link Comment}.
 * Used for returning information about Comment.
 *
 * @author DmitrySheyko
 */
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String text;
    private String authorName;
    private String created;

}


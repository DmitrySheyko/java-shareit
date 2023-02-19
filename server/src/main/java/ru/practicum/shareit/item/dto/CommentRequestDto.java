package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Comment;

/**
 * Class of dto for entity {@link Comment}.
 * Used for getting requests for comments..
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private Long id;
    private String text;
    private Long item;
    private Long author;

}

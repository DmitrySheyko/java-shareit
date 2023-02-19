package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Class of dto for entity {@link ItemRequest}.
 * Used for returning information about ItemRequest.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputItemRequestDto {

    private Long id;
    private String description;
    private String created;
    private List<AnswerDto> items;

}

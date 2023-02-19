package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * Class of dto for entity {@link ItemRequest}.
 * Used for getting information about ItemRequest.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InputItemRequestDto {

    private String description;
    private Long requestor;

}

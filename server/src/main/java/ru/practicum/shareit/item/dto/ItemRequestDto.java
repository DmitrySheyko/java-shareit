package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

/**
 * Class of dto for entity {@link Item}.
 * Used for getting requests for Item.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;
    private String name;
    private String description;
    private Long owner;
    private Boolean available;
    private Long requestId;

}


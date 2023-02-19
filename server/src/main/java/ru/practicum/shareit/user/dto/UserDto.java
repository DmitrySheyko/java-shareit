package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

/**
 * Class of dto for entity {@link User}
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String name;
    private String email;

}

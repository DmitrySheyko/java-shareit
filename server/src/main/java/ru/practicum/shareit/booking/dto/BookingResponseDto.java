package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * Class of dto for entity {@link Booking}.
 * Used for returning information about booking.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    private Long id;
    private String start;
    private String end;
    private ItemResponseDto item;
    private UserDto booker;
    private BookingStatus status;

}

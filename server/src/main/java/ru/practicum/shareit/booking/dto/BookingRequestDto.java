package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

/**
 * Class of dto for entity {@link Booking}.
 * Used for getting booking requests.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {

    private Long id;
    private Long bookerId;
    private Long itemId;
    private String start;
    private String end;
    private BookingStatus status;

}

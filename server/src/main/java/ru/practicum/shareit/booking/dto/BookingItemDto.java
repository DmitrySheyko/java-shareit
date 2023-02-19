package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.booking.model.Booking;

/**
 * Class of short version dto for entity {@link Booking}
 *
 * @author DmitrySheyko
 */
@Getter
@Builder
@AllArgsConstructor
public class BookingItemDto {

    private Long id;
    private Long bookerId;

}

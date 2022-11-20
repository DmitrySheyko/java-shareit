package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class BookingItemDto {
    private Long id;
    private Long bookerId;

}

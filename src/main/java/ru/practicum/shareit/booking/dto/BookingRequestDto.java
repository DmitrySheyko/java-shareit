package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;

@Getter
@Setter
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

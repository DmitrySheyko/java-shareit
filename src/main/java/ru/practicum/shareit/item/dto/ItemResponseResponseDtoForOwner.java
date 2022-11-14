package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseResponseDtoForOwner implements ResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<CommentResponseDto> comments;
}

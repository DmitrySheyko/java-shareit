package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.List;

@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ItemResponseDtoForOwner implements ResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<CommentResponseDto> comments;
}

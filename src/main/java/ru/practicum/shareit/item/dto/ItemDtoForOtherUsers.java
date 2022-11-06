package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.interfaces.Dto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoForOtherUsers implements Dto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private ItemRequest request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentResponseDto> comments;
}

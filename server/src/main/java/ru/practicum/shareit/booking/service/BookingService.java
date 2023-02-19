package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * Interface of service class for entity {@link Booking}.
 *
 * @author DmitrySheyko
 */
public interface BookingService {

    BookingResponseDto add(BookingRequestDto bookingRequestDto);

    BookingResponseDto update(Long userId, Long bookingId, Boolean isApproved);

    BookingResponseDto getById(Long userId, Long bookingId);

    List<BookingResponseDto> getAllBookingsByBookerId(Long userId, String state, int from, int size);

    List<BookingResponseDto> getAllBookingsByOwnerItems(Long userId, String state, int from, int size);

}

package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @PostMapping
    BookingResponseDto add(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                           @RequestBody BookingRequestDto bookingRequestDto) {
        bookingRequestDto.setBookerId(bookerId);
        return bookingServiceImpl.add(bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable(value = "bookingId") Long bookingId,
                              @RequestParam(value = "approved") Boolean isApproved) {
        return bookingServiceImpl.update(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    BookingResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable(value = "bookingId") Long bookingId) {
        return bookingServiceImpl.getById(userId, bookingId);
    }

    @GetMapping
    List<BookingResponseDto> getAllBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(value = "state", defaultValue = "ALL")
                                                      String state) {
        return bookingServiceImpl.getAllBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    List<BookingResponseDto> getAllBookingsByOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(value = "state", defaultValue = "ALL")
                                                        String state) {
        return bookingServiceImpl.getAllBookingsByOwnerItems(userId, state);
    }
}

package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * Class of controller for entity {@link Booking}
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto add(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                  @RequestBody BookingRequestDto bookingRequestDto) {
        bookingRequestDto.setBookerId(bookerId);
        return bookingService.add(bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable(value = "bookingId") Long bookingId,
                                     @RequestParam(value = "approved") Boolean isApproved) {
        return bookingService.update(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable(value = "bookingId") Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @RequestParam(value = "state", defaultValue = "ALL")
                                                             String state,
                                                             @RequestParam(value = "from", required = false,
                                                                     defaultValue = "0") int from,
                                                             @RequestParam(value = "size", required = false,
                                                                     defaultValue = "10") int size) {
        return bookingService.getAllBookingsByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsByOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @RequestParam(value = "state", defaultValue = "ALL")
                                                               String state,
                                                               @RequestParam(value = "from", required = false,
                                                                       defaultValue = "0") int from,
                                                               @RequestParam(value = "size", required = false,
                                                                       defaultValue = "10") int size) {
        return bookingService.getAllBookingsByOwnerItems(userId, state, from, size);
    }

}

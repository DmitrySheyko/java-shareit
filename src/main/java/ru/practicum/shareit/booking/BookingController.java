package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    BookingDto add(@RequestHeader("X-Sharer-User-Id") Long bookerId, @RequestBody BookingDto bookingDto){
        bookingDto.setBooker(bookerId);
        return bookingService.add(bookingDto);
    }
}

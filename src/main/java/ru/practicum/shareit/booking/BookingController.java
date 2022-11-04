package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    BookingResponseDto add(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                           @RequestBody BookingRequestDto bookingRequestDto){
        bookingRequestDto.setBookerId(bookerId);
        return bookingService.add(bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable(value = "bookingId") Long bookingId,
                              @RequestParam(value = "approved") Boolean isApproved){
        return bookingService.update(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    BookingResponseDto getById (@RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable(value = "bookingId") Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    List<BookingResponseDto> getAllBookingsByBookerId (@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(value = "state", defaultValue = "ALL") String state) {
        System.out.println("1");
    return bookingService.getAllBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    List<BookingResponseDto> getAllBookingsByOwnerItems (@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(value = "state", defaultValue = "ALL") String state) {
    return bookingService.getAllBookingsByOwnerItems(userId, state);
    }
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> add(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.add(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Positive @PathVariable(value = "bookingId") Long bookingId,
                                         @RequestParam(value = "approved") Boolean isApproved) {
        return bookingClient.update(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Positive @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByBookerId(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(value = "state", defaultValue = "ALL")
                                                           String stateParam,
                                                           @PositiveOrZero @RequestParam(value = "from", required = false, defaultValue = "0") int from,
                                                           @Positive @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingsByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwnerItems(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @RequestParam(value = "state", defaultValue = "ALL")
                                                             String stateParam,
                                                             @PositiveOrZero @RequestParam(value = "from", required = false, defaultValue = "0") int from,
                                                             @Positive @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingsByOwnerItems(userId, state, from, size);
    }
}
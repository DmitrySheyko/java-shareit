package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class BookingMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final UserService userService;
    private final ItemService itemService;

    public BookingResponseDto entityToResponseDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setBooker(booking.getBooker());
        bookingResponseDto.setItem(booking.getItem());
        bookingResponseDto.setStart(instantToString(booking.getStart()));
        bookingResponseDto.setEnd(instantToString(booking.getEnd()));
        bookingResponseDto.setStatus(booking.getStatus());
        return bookingResponseDto;
    }

    public Booking requestDtoToEntity(BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setBooker(userService.findById(bookingRequestDto.getBookerId()));
        booking.setItem(itemService.findById(bookingRequestDto.getItemId()));
        booking.setStart(stringToInstant(bookingRequestDto.getStart()));
        booking.setEnd(stringToInstant(bookingRequestDto.getEnd()));
        booking.setStatus(bookingRequestDto.getStatus());
        return booking;
    }

    private String instantToString(Instant instantTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instantTime, ZoneId.systemDefault());
        return DATE_TIME_PATTERN.withZone(ZoneId.systemDefault()).format(zonedDateTime);
    }

    private Instant stringToInstant(String stringTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(stringTime, DATE_TIME_PATTERN);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }
}

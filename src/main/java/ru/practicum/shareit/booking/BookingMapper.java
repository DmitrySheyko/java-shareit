package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final static DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        } else {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setId(booking.getId());
            bookingDto.setBooker(booking.getBooker());
            bookingDto.setItemId(booking.getItemId());
            bookingDto.setStart(instantToString(booking.getStart()));
            bookingDto.setEnd(instantToString(booking.getEnd()));
            bookingDto.setStatus(booking.getStatus());
            return bookingDto;
        }
    }

    public Booking toBooking(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        } else {
            Booking booking = new Booking();
            booking.setId(bookingDto.getId());
            booking.setBooker(bookingDto.getBooker());
            booking.setItemId(bookingDto.getItemId());
            booking.setStart(stringToInstant(bookingDto.getStart()));
            booking.setEnd(stringToInstant(bookingDto.getEnd()));
            booking.setStatus(bookingDto.getStatus());
            return booking;
        }
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
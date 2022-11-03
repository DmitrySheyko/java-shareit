package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    public BookingDto add(BookingDto bookingDto) {
        if (!userService.checkIsObjectInStorage(bookingDto.getBooker())) {
            log.warn(String.format("Пользователь id=%s не найден.", bookingDto.getBooker()));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", bookingDto.getBooker()));
        }
        if (!checkIsItemInStorageAndAvailable(bookingDto.getItemId())) {
            log.warn(String.format("Объект id=%s не доступен для бронирования.", bookingDto.getBooker()));
            throw new ValidationException(String.format("Объект id=%s не доступен для бронирования.", bookingDto.getBooker()));
        }
        Booking booking = bookingMapper.toBooking(bookingDto);
        checkBookingTime(booking);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        log.info(String.format("Бронирование id=%s успешно добавлено.", savedBooking.getId()));
        return bookingMapper.toDto(savedBooking);
    }


    private Boolean checkIsItemInStorageAndAvailable(long itemId) {
        if (!itemService.checkIsObjectInStorage(itemId)) {
            log.warn(String.format("Объект id=%s не найден.", itemId));
            throw new ObjectNotFoundException(String.format("Объект id=%s не найден.", itemId));
        } else {
            return itemService.getById(itemId).getAvailable();
        }
    }

    private void checkBookingTime(Booking booking) {
        if (booking.getStart().isBefore(Instant.now())) {
            log.warn(String.format("Некорректное время начала бронирования объекта id=%s.", booking.getBooker()));
            throw new ValidationException(String.format("ОНекорректное время начала бронирования объекта id=%s.", booking.getBooker()));
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            log.warn(String.format("Некорректное время окончания бронирования объекта id=%s.", booking.getBooker()));
            throw new ValidationException(String.format("ОНекорректное время окончания бронирования объекта id=%s.", booking.getBooker()));
        }
    }
}
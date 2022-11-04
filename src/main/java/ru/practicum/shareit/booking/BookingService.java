package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDtoForOtherUsers;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    public BookingResponseDto add(BookingRequestDto bookingRequestDto) {
        if (!userService.checkIsObjectInStorage(bookingRequestDto.getBookerId())) {
            log.warn(String.format("Пользователь id=%s не найден.", bookingRequestDto.getBookerId()));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", bookingRequestDto.getBookerId()));
        }
        if (!checkIsItemInStorageAndAvailable(bookingRequestDto.getItemId())) {
            log.warn(String.format("Объект id=%s не доступен для бронирования.", bookingRequestDto.getBookerId()));
            throw new ValidationException(String.format("Объект id=%s не доступен для бронирования.", bookingRequestDto.getBookerId()));
        }
        if (checkIsUserOwnerOfItem(bookingRequestDto.getBookerId(), bookingRequestDto.getItemId())){
            log.warn(String.format("Пользователь id=%s не может забронировать свою вещь id=%s.", bookingRequestDto.getBookerId(), bookingRequestDto.getItemId()));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не может забронировать свою вещь id=%s.", bookingRequestDto.getBookerId(), bookingRequestDto.getItemId()));
        }
        bookingRequestDto.setStatus(BookingStatus.WAITING);
        Booking booking = bookingMapper.requestDtoToEntity(bookingRequestDto);
        checkBookingTime(booking);
        Booking savedBooking = bookingRepository.save(booking);
        log.info(String.format("Бронирование id=%s успешно добавлено.", savedBooking.getId()));
        return bookingMapper.EntityToResponseDto(savedBooking);
    }

    public BookingResponseDto update(Long userId, Long bookingId, Boolean isApproved) {
        if (!userService.checkIsObjectInStorage(userId)) {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
        if (!checkIsObjectInStorage(bookingId)) {
            log.warn(String.format("Бронирование id=%s не найдено.", bookingId));
            throw new ValidationException(String.format("Бронирование id=%s не найдено.", bookingId));
        }
        if(!checkIsUserOwnerOfBookedItem(userId, bookingId)){
            log.warn(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
            throw new ObjectNotFoundException(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
        }
        BookingResponseDto bookingResponseDto = getById(userId, bookingId);
        if (bookingResponseDto.getStatus().equals(BookingStatus.APPROVED)){
            log.warn(String.format("Статус бронирование id=%s не может  быть изменен.", bookingId));
            throw new ValidationException(String.format("Статус бронирование id=%s не может  быть изменен.", bookingId));
        }
        Booking booking = bookingMapper.responseDtoToEntity(bookingResponseDto);
        if (userId.equals(bookingResponseDto.getItem().getOwner())) {
            booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        }
        if (userId.equals(bookingResponseDto.getBooker().getId())) {
            booking.setStatus(isApproved ? BookingStatus.WAITING : BookingStatus.CANCELED);
        }
        Booking updatedBooking = bookingRepository.save(booking);
        bookingResponseDto.setStatus(updatedBooking.getStatus());
        return bookingResponseDto;
    }

    public BookingResponseDto getById(Long userId, Long bookingId) {
        if (!userService.checkIsObjectInStorage(userId)) {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
        if (!checkIsObjectInStorage(bookingId)) {
            log.warn(String.format("Бронирование id=%s не найдено.", bookingId));
            throw new ObjectNotFoundException(String.format("Бронирование id=%s не найдено.", bookingId));
        }
        if(! (checkIsUserCreatorOfBooking(userId, bookingId) || checkIsUserOwnerOfBookedItem(userId, bookingId))){
            log.warn(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
            throw new ObjectNotFoundException(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
        }
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking;
        if (optionalBooking.isPresent()) {
            booking = optionalBooking.get();
            return bookingMapper.EntityToResponseDto(booking);
        }
        log.warn(String.format("Ифнормация о бронировании id=%s не найдена.", bookingId));
        throw new ObjectNotFoundException(String.format("Ифнормация о бронировании id=%s не найдена.", bookingId));
    }

    List<BookingResponseDto> getAllBookingsByBookerId(Long userId, String state) {
        if (!userService.checkIsObjectInStorage(userId)) {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
        System.out.println("2");
        List<Booking> result;
        switch (state) {
            case ("ALL"): {
                result = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            }
            case ("CURRENT"): {
                result = bookingRepository.findAllCurrentByBookerId(userId, Instant.now());
                break;
            }
            case ("FUTURE"): {
                result = bookingRepository.findAllFutureByBookerId(userId, Instant.now());
                break;
            }
            case ("PAST"): {
                result = bookingRepository.findAllPastByBookerId(userId, Instant.now());
                break;
            }
            case ("WAITING"): {
                System.out.println("3");
                result = bookingRepository.findAllWaitingByBookerId(userId, BookingStatus.valueOf(state));
                break;
            }
            case ("REJECTED"): {
                result = bookingRepository.findAllRejectedByBookerId(userId, BookingStatus.valueOf(state));
                break;
            }
            default: {
                log.warn("Unknown state: UNSUPPORTED_STATUS");
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        System.out.println("4");
        return result.stream().map(bookingMapper::EntityToResponseDto).collect(Collectors.toList());
    }

    List<BookingResponseDto> getAllBookingsByOwnerItems(Long userId, String state) {
        if (!userService.checkIsObjectInStorage(userId)) {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
        List<Booking> result;
        switch (state) {
            case ("ALL"): {
                result = bookingRepository.findAllBookingsByItemOwner(userId);
                break;
            }
            case ("CURRENT"): {
                result = bookingRepository.findAllCurrentBookingsByItemOwner(userId, Instant.now());
                break;
            }
            case ("FUTURE"): {
                result = bookingRepository.findAllFutureBookingsByItemOwner(userId, Instant.now());
                break;
            }
            case ("PAST"): {
                result = bookingRepository.findAllPastBookingsByItemOwner(userId, Instant.now());
                break;
            }
            case ("WAITING"): {
                result = bookingRepository.findAllWaitingBookingsByItemOwner(userId, BookingStatus.valueOf(state));
                break;
            }
            case ("REJECTED"): {
                result = bookingRepository.findAllRejectedBookingsByItemOwner(userId, BookingStatus.valueOf(state));
                break;
            }
            default: {
                log.warn("Unknown state: UNSUPPORTED_STATUS");
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        System.out.println(result);
        return result.stream().map(bookingMapper::EntityToResponseDto).collect(Collectors.toList());
    }

    private boolean checkIsItemInStorageAndAvailable(long itemId) {
        if (!itemService.checkIsObjectInStorage(itemId)) {
            log.warn(String.format("Объект id=%s не найден.", itemId));
            throw new ObjectNotFoundException(String.format("Объект id=%s не найден.", itemId));
        } else {
            return itemService.findById(itemId).getAvailable();
        }
    }

    private void checkBookingTime(Booking booking) {
        if (booking.getStart().isBefore(Instant.now())) {
            log.warn(String.format("Некорректное время начала бронирования объекта id=%s.", booking.getBookerId()));
            throw new ValidationException(String.format("ОНекорректное время начала бронирования объекта id=%s.", booking.getBookerId()));
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            log.warn(String.format("Некорректное время окончания бронирования объекта id=%s.", booking.getBookerId()));
            throw new ValidationException(String.format("ОНекорректное время окончания бронирования объекта id=%s.", booking.getBookerId()));
        }
    }

    public boolean checkIsObjectInStorage(Long bookingId) {
        return bookingRepository.existsById(bookingId);
    }

    public boolean checkIsObjectInStorage(BookingRequestDto bookingRequestDto) {
        return bookingRepository.existsById(bookingRequestDto.getId());
    }

    public boolean checkIsUserCreatorOfBooking(Long userId, Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking;
        if (optionalBooking.isPresent()) {
            booking = optionalBooking.get();
        } else {
            log.warn(String.format("Информация о бронировании id=%s не найдена.", bookingId));
            throw new ObjectNotFoundException(String.format("Информация о бронировании id=%s не найдена.", bookingId));
        }
        return userId.equals(booking.getBookerId());
    }

    public boolean checkIsUserOwnerOfItem(Long userId, Long itemId) {
        Item item = itemService.findById(itemId);
        return userId.equals(item.getOwner());
    }

    public boolean checkIsUserOwnerOfBookedItem(Long userId, Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking;
        if (optionalBooking.isPresent()) {
            booking = optionalBooking.get();
        } else {
            log.warn(String.format("Бронирование id=%s не найдено.", bookingId));
            throw new ObjectNotFoundException(String.format("Бронирование id=%s не найдено.", bookingId));
        }
        Item item = itemService.findById(booking.getItemId());
        return userId.equals(item.getOwner());
    }
}
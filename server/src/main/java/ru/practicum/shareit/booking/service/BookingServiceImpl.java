package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserServiceImpl userServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @Override
    public BookingResponseDto add(BookingRequestDto bookingRequestDto) {
        userServiceImpl.checkIsObjectInStorage(bookingRequestDto.getBookerId());
        itemServiceImpl.checkIsItemInStorage(bookingRequestDto.getItemId());
        itemServiceImpl.checkIsItemAvailable(bookingRequestDto.getItemId());
        checkBookingTime(bookingRequestDto);
        Booking booking = bookingMapper.requestDtoToEntity(bookingRequestDto);
        checkIsItemCanBeBooked(booking.getItem().getId(), booking.getStart(), booking.getEnd());
        if (checkIsUserOwnerOfItem(booking.getBooker().getId(), booking.getItem().getId())) {
            log.warn("Бронирование своих вещей не доступно.");
            throw new ObjectNotFoundException("Бронирование своих вещей не доступно.");
        }
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        log.info(String.format("Бронирование id=%s успешно добавлено.", savedBooking.getId()));
        return bookingMapper.entityToResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto update(Long userId, Long bookingId, Boolean isApproved) {
        userServiceImpl.checkIsObjectInStorage(userId);
        checkIsBookingInStorage(bookingId);
        checkIsUserOwnerOfBookedItem(userId, bookingId);
        Booking bookingForUpdate = findById(bookingId);
        if (bookingForUpdate.getStatus() == (BookingStatus.APPROVED)) {
            log.warn(String.format("Статус бронирование id=%s не может  быть изменен.", bookingId));
            throw new ValidationException(String.format("Статус бронирование id=%s не может  быть изменен.",
                    bookingId));
        }
        if (userId.equals(itemServiceImpl.findById(bookingForUpdate.getItem().getId()).getOwner())) {
            bookingForUpdate.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        }
        Booking updatedBooking = bookingRepository.save(bookingForUpdate);
        BookingResponseDto bookingResponseDto = bookingMapper.entityToResponseDto(updatedBooking);
        log.info(String.format("Бронирование id=%s успешно обновлено.", bookingId));
        return bookingResponseDto;
    }

    @Override
    public BookingResponseDto getById(Long userId, Long bookingId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        checkIsBookingInStorage(bookingId);
        if (!(checkIsUserCreatorOfBooking(userId, bookingId) || checkIsUserOwnerOfBookedItem(userId, bookingId))) {
            log.warn(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
            throw new ObjectNotFoundException(String.format("Данные о бронировании не доступны  для " +
                    "пользователя id=%s.", userId));
        }
        Booking booking = findById(bookingId);
        BookingResponseDto bookingResponseDto = bookingMapper.entityToResponseDto(booking);
        log.info(String.format("Данные о бронировании id=%s успешно получено.", bookingId));
        return bookingResponseDto;
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByBookerId(Long userId, String state, int from, int size) {
        userServiceImpl.checkIsObjectInStorage(userId);
        RequestState stateForSearch = checkStateForSearch(state);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());
        Page<Booking> result;
        switch (stateForSearch) {
            case ALL: {
                result = bookingRepository.findAllByBookerId(pageable, userId);
                break;
            }
            case CURRENT: {
                result = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(pageable, userId,
                        Instant.now(), Instant.now());
                break;
            }
            case FUTURE: {
                result = bookingRepository.findAllByBookerIdAndStartAfter(pageable, userId, Instant.now());
                break;
            }
            case PAST: {
                result = bookingRepository.findAllByBookerIdAndEndBefore(pageable, userId, Instant.now());
                break;
            }
            case WAITING: {
                result = bookingRepository.findAllByBookerIdAndStatus(pageable, userId, BookingStatus.WAITING);
                break;
            }
            case REJECTED: {
                result = bookingRepository.findAllByBookerIdAndStatus(pageable, userId, BookingStatus.REJECTED);
                break;
            }
            default: {
                log.warn("Unknown state: UNSUPPORTED_STATUS");
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        return result.stream().map(bookingMapper::entityToResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByOwnerItems(Long userId, String state, int from, int size) {
        userServiceImpl.checkIsObjectInStorage(userId);
        RequestState stateForSearch = checkStateForSearch(state);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());
        Page<Booking> result;
        switch (stateForSearch) {
            case ALL: {
                result = bookingRepository.findAllByItemOwner(pageable, userId);
                break;
            }
            case CURRENT: {
                result = bookingRepository.findAllByItemOwnerAndStartLessThanAndEndGreaterThan(pageable, userId,
                        Instant.now(), Instant.now());
                break;
            }
            case FUTURE: {
                result = bookingRepository.findAllByItemOwnerAndStartGreaterThan(pageable, userId, Instant.now());
                break;
            }
            case PAST: {
                result = bookingRepository.findAllByItemOwnerAndEndLessThan(pageable, userId, Instant.now());
                break;
            }
            case WAITING: {
                result = bookingRepository.findAllByItemOwnerAndStatus(pageable, userId, BookingStatus.WAITING);
                break;
            }
            case REJECTED: {
                result = bookingRepository.findAllByItemOwnerAndStatus(pageable, userId, BookingStatus.REJECTED);
                break;
            }
            default: {
                log.warn("Unknown state: UNSUPPORTED_STATUS");
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        return result.stream().map(bookingMapper::entityToResponseDto).collect(Collectors.toList());
    }

    private Booking findById(Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            return optionalBooking.get();
        } else {
            log.warn(String.format("Ифнормация о бронировании id=%s не найдена.", bookingId));
            throw new ObjectNotFoundException(String.format("Ифнормация о бронировании id=%s не найдена.", bookingId));
        }
    }

    private void checkIsItemCanBeBooked(Long itemId, Instant start, Instant end) {
        Optional<Booking> optionalBooking = bookingRepository
                .findByItemIdAndEndAfterAndStartBeforeOrderByStartDesc(itemId, start, end);
        if (!optionalBooking.isEmpty()) {
            log.warn(String.format("Объект itemId=%s не доступен для бронирования.", itemId));
            throw new ValidationException(String.format("Объект itemId=%s не доступен для бронирования.", itemId));
        }
    }

    private void checkBookingTime(BookingRequestDto booking) {
        if (booking == null || booking.getStart() == null || booking.getEnd() == null) {
            log.warn("Некорректное время начала бронирования объекта.");
            throw new ValidationException("Некорректное время начала бронирования объекта.");
        }
        Instant start = stringToInstant(booking.getStart());
        Instant end = stringToInstant(booking.getEnd());
        if (start.isBefore(Instant.now())) {
            log.warn(String.format("Некорректное время начала бронирования объекта id=%s.", booking.getItemId()));
            throw new ValidationException(String.format("Некорректное время начала бронирования объекта id=%s.",
                    booking.getItemId()));
        }
        if (end.isBefore(start) || end.equals(start)) {
            log.warn(String.format("Некорректное время окончания бронирования объекта id=%s.", booking.getItemId()));
            throw new ValidationException(String.format("Некорректное время окончания бронирования объекта id=%s.",
                    booking.getItemId()));
        }
    }

    private void checkIsBookingInStorage(Long bookingId) {
        if (!(bookingId != null && bookingRepository.existsById(bookingId))) {
            log.warn(String.format("Бронирование id=%s не найдено.", bookingId));
            throw new ObjectNotFoundException(String.format("Бронирование id=%s не найдено.", bookingId));
        }
    }

    private boolean checkIsUserCreatorOfBooking(Long userId, Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            return userId.equals(optionalBooking.get().getBooker().getId());
        } else {
            log.warn(String.format("Информация о бронировании id=%s не найдена.", bookingId));
            throw new ObjectNotFoundException(String.format("Информация о бронировании id=%s не найдена.", bookingId));
        }
    }

    private boolean checkIsUserOwnerOfItem(Long userId, Long itemId) {
        Item item = itemServiceImpl.findById(itemId);
        return userId.equals(item.getOwner());
    }

    private boolean checkIsUserOwnerOfBookedItem(Long userId, Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking;
        if (optionalBooking.isPresent()) {
            booking = optionalBooking.get();
        } else {
            log.warn(String.format("Бронирование id=%s не найдено.", bookingId));
            throw new ObjectNotFoundException(String.format("Бронирование id=%s не найдено.", bookingId));
        }
        if (userId.equals(booking.getItem().getOwner())) {
            return true;
        } else {
            log.warn(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
            throw new ObjectNotFoundException(String.format("Данные о бронировании не доступны  для пользователя " +
                    "id=%s.", userId));
        }
    }

    private Instant stringToInstant(String stringTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(stringTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }

    private RequestState checkStateForSearch(String state) {
        RequestState stateForSearch;
        try {
            stateForSearch = RequestState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return stateForSearch;
    }
}
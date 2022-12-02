package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    private final BookingServiceImpl bookingService;
    private BookingRequestDto bookingRequestDto;
    private final Long bookerId = 3L;
    private final Long itemId = 1L;
    private final Long itemOwnerId = 1L;
    private String start = "2024-07-11T10:10:10";
    private String end = "2024-08-11T10:10:10";

    @BeforeEach
    void init() {
        bookingRequestDto = BookingRequestDto.builder().bookerId(bookerId).start(start).end(end)
                .itemId(itemId).build();
    }

    @Test
    void add() {
        BookingResponseDto resultOfAdding = bookingService.add(bookingRequestDto);
        Assertions.assertNotNull(resultOfAdding.getId());
        Assertions.assertEquals(itemId, resultOfAdding.getItem().getId());
        Assertions.assertEquals(start, resultOfAdding.getStart());
        Assertions.assertEquals(end, resultOfAdding.getEnd());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingOwnerOfItem() {
        Long ownerOfItem = 1L;
        BookingRequestDto bookingFromOwner = BookingRequestDto.builder().bookerId(ownerOfItem).start(start).end(end)
                .itemId(itemId).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.add(bookingFromOwner));
        Assertions.assertEquals("Бронирование своих вещей не доступно.", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNotExistsBooker() {
        Long notExistsBookerId = 100L;
        BookingRequestDto bookingRequestDtoWithNotExistsBookerId = BookingRequestDto.builder()
                .bookerId(notExistsBookerId).start(start).end(end).itemId(itemId).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.add(bookingRequestDtoWithNotExistsBookerId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsBookerId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullBooker() {
        Long nullBookerId = null;
        BookingRequestDto bookingRequestDtoWithNullBookerId = BookingRequestDto.builder().bookerId(nullBookerId)
                .start(start).end(end).itemId(itemId).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .add(bookingRequestDtoWithNullBookerId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullBookerId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNotExistsItemId() {
        Long notExistsItemId = 100L;
        BookingRequestDto bookingRequestDtoWithNotExistsItemId = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(notExistsItemId).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.add(bookingRequestDtoWithNotExistsItemId));
        Assertions.assertEquals(String.format("Объект itemId=%s не найден", notExistsItemId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullItemId() {
        Long nullItemId = null;
        BookingRequestDto bookingRequestDtoWithNullItemId = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(nullItemId).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.add(bookingRequestDtoWithNullItemId));
        Assertions.assertEquals(String.format("Объект itemId=%s не найден", nullItemId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullStart() {
        String nullStart = null;
        BookingRequestDto bookingRequestDtoWithNullStart = BookingRequestDto.builder()
                .bookerId(bookerId).start(nullStart).end(end).itemId(itemId).build();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithNullStart));
        Assertions.assertEquals("Некорректное время начала бронирования объекта.", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullEnd() {
        String nullEnd = null;
        BookingRequestDto bookingRequestDtoWithNullEnd = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(nullEnd).itemId(itemId).build();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithNullEnd));
        Assertions.assertEquals("Некорректное время начала бронирования объекта.", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithStartInPast() {
        start = "2021-08-11T10:10:10";
        BookingRequestDto bookingRequestDtoWithNullEnd = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(itemId).build();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithNullEnd));
        Assertions.assertEquals(String.format("Некорректное время начала бронирования объекта id=%s.", itemId),
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithEndBeforeStart() {
        start = "2023-08-11T10:10:10";
        end = "2024-08-11T10:10:10";
        BookingRequestDto bookingRequestDtoWithEndBeforeStart = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(itemId).build();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithEndBeforeStart));
        Assertions.assertEquals(String.format("Объект itemId=%s не доступен для бронирования.", itemId),
                thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithStartEqualsEnd() {
        start = "2023-08-11T10:10:10";
        end = "2023-08-11T10:10:10";
        BookingRequestDto bookingRequestDtoWithEndBeforeStart = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(itemId).build();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithEndBeforeStart));
        Assertions.assertEquals("Некорректное время окончания бронирования объекта id=1.",
                thrown.getMessage());
    }

    @Test
    void update() {
        BookingResponseDto resultOfAdding = bookingService.add(bookingRequestDto);
        BookingResponseDto resultOfUpdate = bookingService.update(itemOwnerId, resultOfAdding.getId(), false);
        Assertions.assertEquals(BookingStatus.REJECTED, resultOfUpdate.getStatus());
        resultOfUpdate = bookingService.update(itemOwnerId, resultOfAdding.getId(), true);
        Assertions.assertEquals(BookingStatus.APPROVED, resultOfUpdate.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenOwnerTryToRejectApprovedBooking() {
        BookingResponseDto resultOfAdding = bookingService.add(bookingRequestDto);
        BookingResponseDto resultOfUpdate = bookingService.update(itemOwnerId, resultOfAdding.getId(), true);
        Assertions.assertEquals(BookingStatus.APPROVED, resultOfUpdate.getStatus());
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> bookingService.update(itemOwnerId,
                        resultOfAdding.getId(), false));
        Assertions.assertEquals(String.format("Статус бронирование id=%s не может  быть изменен.",
                resultOfAdding.getId()), thrown.getMessage());
        Assertions.assertEquals(BookingStatus.APPROVED, resultOfUpdate.getStatus());
    }

    @Test
    void getById() {
        BookingResponseDto resultOfAdding = bookingService.add(bookingRequestDto);

        BookingResponseDto resultOfRequestFromItemOwner = bookingService.getById(itemOwnerId,
                resultOfAdding.getId());
        Assertions.assertEquals(resultOfAdding.getId(), resultOfRequestFromItemOwner.getId());

        BookingResponseDto resultOfRequestFromBookingCreator = bookingService.getById(bookerId,
                resultOfAdding.getId());
        Assertions.assertEquals(resultOfAdding.getId(), resultOfRequestFromBookingCreator.getId());

        Long anotherUser = 4L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getById(anotherUser,
                        resultOfAdding.getId()));
        Assertions.assertEquals(String.format("Данные о бронировании не доступны  для пользователя id=%s.",
                anotherUser), thrown.getMessage());

        Long nullUser = null;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(nullUser,
                resultOfAdding.getId()));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullUser), thrown.getMessage());

        Long notExistsUser = 100L;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(notExistsUser,
                resultOfAdding.getId()));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsUser), thrown.getMessage());

        Long notExistsBooking = 100L;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(bookerId,
                notExistsBooking));
        Assertions.assertEquals(String.format("Бронирование id=%s не найдено.", notExistsBooking), thrown.getMessage());
    }

    @Test
    void getAllBookingsByBookerId() {
        Long bookerId = 1L;
        int from = 0;
        int size = 10;
        List<BookingResponseDto> allBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.ALL.toString(), from, size);
        List<BookingResponseDto> pastBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.PAST.toString(), from, size);
        List<BookingResponseDto> futureBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.FUTURE.toString(), from, size);
        List<BookingResponseDto> waitingBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.WAITING.toString(), from, size);
        List<BookingResponseDto> rejectedBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.REJECTED.toString(), from, size);
        List<BookingResponseDto> currentBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.CURRENT.toString(), from, size);
        Assertions.assertEquals(4, allBookings.size());
        Assertions.assertEquals(2, pastBookings.size());
        Assertions.assertEquals(1, futureBookings.size());
        Assertions.assertEquals(2, waitingBookings.size());
        Assertions.assertEquals(2, rejectedBookings.size());
        Assertions.assertEquals(1, currentBookings.size());

        Long nullBookerId = null;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByBookerId(nullBookerId, RequestState.ALL.toString(), from, size));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullBookerId), thrown.getMessage());

        Long notExistsBookerId = null;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByBookerId(notExistsBookerId, RequestState.ALL.toString(), from, size));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsBookerId), thrown.getMessage());

        String incorrectBookingStatus = "INCORRECT";
        ValidationException thrown1 = Assertions.assertThrows(ValidationException.class, () -> bookingService
                .getAllBookingsByBookerId(bookerId, incorrectBookingStatus, from, size));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", thrown1.getMessage());
    }

    @Test
    void getAllBookingsByOwnerItems() {
        Long itemOwnerId = 1L;
        int from = 0;
        int size = 10;
        List<BookingResponseDto> allBookings = bookingService.getAllBookingsByOwnerItems(itemOwnerId,
                RequestState.ALL.toString(), from, size);
        List<BookingResponseDto> pastBookings = bookingService.getAllBookingsByOwnerItems(itemOwnerId,
                RequestState.PAST.toString(), from, size);
        List<BookingResponseDto> futureBookings = bookingService.getAllBookingsByOwnerItems(itemOwnerId,
                RequestState.FUTURE.toString(), from, size);
        List<BookingResponseDto> waitingBookings = bookingService.getAllBookingsByOwnerItems(itemOwnerId,
                RequestState.WAITING.toString(), from, size);
        List<BookingResponseDto> rejectedBookings = bookingService.getAllBookingsByOwnerItems(itemOwnerId,
                RequestState.REJECTED.toString(), from, size);
        List<BookingResponseDto> currentBookings = bookingService.getAllBookingsByOwnerItems(itemOwnerId,
                RequestState.CURRENT.toString(), from, size);
        Assertions.assertEquals(4, allBookings.size());
        Assertions.assertEquals(2, pastBookings.size());
        Assertions.assertEquals(1, futureBookings.size());
        Assertions.assertEquals(2, waitingBookings.size());
        Assertions.assertEquals(1, rejectedBookings.size());
        Assertions.assertEquals(1, currentBookings.size());

        Long nullBookerId = null;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByOwnerItems(nullBookerId, RequestState.ALL.toString(), from, size));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullBookerId), thrown.getMessage());

        Long notExistsBookerId = null;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByOwnerItems(notExistsBookerId, RequestState.ALL.toString(), from, size));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsBookerId), thrown.getMessage());

        String incorrectBookingStatus = "INCORRECT";
        ValidationException thrown1 = Assertions.assertThrows(ValidationException.class, () -> bookingService
                .getAllBookingsByOwnerItems(itemOwnerId, incorrectBookingStatus, from, size));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", thrown1.getMessage());
    }
}
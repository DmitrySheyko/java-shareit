package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    final BookingServiceImpl bookingService;
    final BookingRepository bookingRepository;
    final BookingMapper bookingMapper;
    final UserServiceImpl userServiceImpl;
    final ItemServiceImpl itemServiceImpl;
    Long bookerId = 3L;
    Long itemId = 1L;
    Long itemOwnerId = 1L;
    String start = "2024-07-11T10:10:10";
    String end = "2024-08-11T10:10:10";

    @Test
    void add() {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().bookerId(bookerId).start(start).end(end)
                .itemId(itemId).build();
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
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.add(bookingFromOwner));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNotExistsBooker() {
        Long notExistsBookerId = 100L;
        BookingRequestDto bookingRequestDtoWithNotExistsBookerId = BookingRequestDto.builder()
                .bookerId(notExistsBookerId).start(start).end(end).itemId(itemId).build();
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.add(bookingRequestDtoWithNotExistsBookerId));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullBooker() {
        Long nullBookerId = null;
        BookingRequestDto bookingRequestDtoWithNullBookerId = BookingRequestDto.builder().bookerId(nullBookerId)
                .start(start).end(end).itemId(itemId).build();
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .add(bookingRequestDtoWithNullBookerId));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNotExistsItemId() {
        Long notExistsItemId = 100L;
        BookingRequestDto bookingRequestDtoWithNotExistsItemId = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(notExistsItemId).build();
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.add(bookingRequestDtoWithNotExistsItemId));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullItemId() {
        Long nullItemId = null;
        BookingRequestDto bookingRequestDtoWithNullItemId = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(nullItemId).build();
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.add(bookingRequestDtoWithNullItemId));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullStart() {
        String nullStart = null;
        BookingRequestDto bookingRequestDtoWithNullStart = BookingRequestDto.builder()
                .bookerId(bookerId).start(nullStart).end(end).itemId(itemId).build();
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithNullStart));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithNullEnd() {
        String nullEnd = null;
        BookingRequestDto bookingRequestDtoWithNullEnd = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(nullEnd).itemId(itemId).build();
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithNullEnd));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithStartInPast() {
        start = "2021-08-11T10:10:10";
        BookingRequestDto bookingRequestDtoWithNullEnd = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(itemId).build();
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithNullEnd));
    }

    @Test
    void shouldThrowExceptionWhenAddBookingWithEndBeforeStart() {
        start = "2023-08-11T10:10:10";
        end = "2024-08-11T10:10:10";
        BookingRequestDto bookingRequestDtoWithEndBeforeStart = BookingRequestDto.builder()
                .bookerId(bookerId).start(start).end(end).itemId(itemId).build();
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.add(bookingRequestDtoWithEndBeforeStart));
    }

    @Test
    void update() {
        BookingRequestDto requestDtoForAdd = BookingRequestDto.builder().bookerId(bookerId).start(start).end(end)
                .itemId(itemId).build();
        BookingResponseDto resultOfAdding = bookingService.add(requestDtoForAdd);
        BookingResponseDto resultOfUpdate = bookingService.update(itemOwnerId, resultOfAdding.getId(), false);
        Assertions.assertEquals(BookingStatus.REJECTED, resultOfUpdate.getStatus());
        resultOfUpdate = bookingService.update(itemOwnerId, resultOfAdding.getId(), true);
        Assertions.assertEquals(BookingStatus.APPROVED, resultOfUpdate.getStatus());
    }

    @Test
    void getById() {
        BookingRequestDto requestDtoForAdd = BookingRequestDto.builder().bookerId(bookerId).start(start).end(end)
                .itemId(itemId).build();
        BookingResponseDto resultOfAdding = bookingService.add(requestDtoForAdd);
        BookingResponseDto resultOfRequestFromItemOwner = bookingService.getById(itemOwnerId,
                resultOfAdding.getId());
        Assertions.assertEquals(resultOfAdding.getId(), resultOfRequestFromItemOwner.getId());

        BookingResponseDto resultOfRequestFromBookingCreator = bookingService.getById(bookerId,
                resultOfAdding.getId());
        Assertions.assertEquals(resultOfAdding.getId(), resultOfRequestFromBookingCreator.getId());

        Long anotherUser = 4L;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(anotherUser,
                resultOfAdding.getId()));

        Long nullUser = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(nullUser,
                resultOfAdding.getId()));

        Long notExistsUser = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(notExistsUser,
                resultOfAdding.getId()));

        Long notExistsBooking = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(bookerId,
                notExistsBooking));
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
        Assertions.assertEquals(4, allBookings.size());
        Assertions.assertEquals(2, pastBookings.size());
        Assertions.assertEquals(1, futureBookings.size());
        Assertions.assertEquals(2, waitingBookings.size());
        Assertions.assertEquals(2, rejectedBookings.size());

        Long nullBookerId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByBookerId(nullBookerId, RequestState.ALL.toString(), from, size));

        Long notExistsBookerId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByBookerId(notExistsBookerId, RequestState.ALL.toString(), from, size));

        String incorrectBookingStatus = "INCORRECT";
        Assertions.assertThrows(ValidationException.class, () -> bookingService
                .getAllBookingsByBookerId(bookerId, incorrectBookingStatus, from, size));
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
        Assertions.assertEquals(4, allBookings.size());
        Assertions.assertEquals(2, pastBookings.size());
        Assertions.assertEquals(1, futureBookings.size());
        Assertions.assertEquals(2, waitingBookings.size());
        Assertions.assertEquals(1, rejectedBookings.size());

        Long nullBookerId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByOwnerItems(nullBookerId, RequestState.ALL.toString(), from, size));

        Long notExistsBookerId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllBookingsByOwnerItems(notExistsBookerId, RequestState.ALL.toString(), from, size));

        String incorrectBookingStatus = "INCORRECT";
        Assertions.assertThrows(ValidationException.class, () -> bookingService
                .getAllBookingsByOwnerItems(itemOwnerId, incorrectBookingStatus, from, size));
    }
}
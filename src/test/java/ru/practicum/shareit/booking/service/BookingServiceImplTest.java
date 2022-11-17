package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.util.List;

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

    @Test
    void add() {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .bookerId(3L)
                .start("2023-07-11T10:10:10")
                .end("2023-08-11T10:10:10")
                .itemId(1L)
                .build();
        BookingResponseDto resultOfAdding = bookingService.add(bookingRequestDto);
        BookingResponseDto resultOfGetting = bookingService.getById(1L, resultOfAdding.getId());
        Assertions.assertEquals(resultOfGetting.getItem(), resultOfAdding.getItem());
        Assertions.assertEquals(resultOfGetting.getStart(), resultOfAdding.getStart());
        Assertions.assertEquals(resultOfGetting.getEnd(), resultOfAdding.getEnd());
    }

    @Test
    void update() {
        BookingRequestDto RequestDtoForAdd = BookingRequestDto.builder()
                .bookerId(3L)
                .start("2023-11-11T10:10:10")
                .end("2023-12-11T10:10:10")
                .itemId(1L)
                .build();
        BookingResponseDto resultOfAdding = bookingService.add(RequestDtoForAdd);
        BookingResponseDto resultOfUpdate = bookingService.update(1L, resultOfAdding.getId(), false);
        Assertions.assertEquals(BookingStatus.REJECTED, resultOfUpdate.getStatus());
        resultOfUpdate = bookingService.update(1L, resultOfAdding.getId(), true);
        Assertions.assertEquals(BookingStatus.APPROVED, resultOfUpdate.getStatus());
    }

    @Test
    void getById() {
        Long itemOwnerId = 2L;
        Long bookingCreatorId = 3L;
        Long itemId = 3L;
        BookingRequestDto RequestDtoForAdd = BookingRequestDto.builder()
                .bookerId(bookingCreatorId)
                .start("2023-09-11T10:10:10")
                .end("2023-10-11T10:10:10")
                .itemId(itemId)
                .build();
        BookingResponseDto resultOfAdding = bookingService.add(RequestDtoForAdd);
        BookingResponseDto resultOfRequestFromItemOwner = bookingService.getById(itemOwnerId,
                resultOfAdding.getId());
        BookingResponseDto resultOfRequestFromBookingCreator = bookingService.getById(bookingCreatorId,
                resultOfAdding.getId());
        Assertions.assertEquals(resultOfAdding.getId(), resultOfRequestFromItemOwner.getId());
        Assertions.assertEquals(resultOfAdding.getId(), resultOfRequestFromBookingCreator.getId());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.getById(1L,
                resultOfAdding.getId()));
    }

    @Test
    void getAllBookingsByBookerId() {
        Long itemId = 3L;
        Long itemOwnerId = 2L;
        Long bookerId = 1L;
        Booking requestForPastBooking = Booking.builder()
                .booker(userServiceImpl.findById(bookerId))
                .item(itemServiceImpl.findById(itemId))
                .start(Instant.parse("2021-01-01T10:10:10.00Z"))
                .end(Instant.parse("2021-12-30T10:10:10.00Z"))
                .status(BookingStatus.WAITING)
                .build();
        Booking requestForCurrentBooking = Booking.builder()
                .booker(userServiceImpl.findById(bookerId))
                .item(itemServiceImpl.findById(itemId))
                .start(Instant.parse("2022-01-01T10:10:10.00Z"))
                .end(Instant.parse("2022-12-30T10:10:10.00Z"))
                .status(BookingStatus.WAITING)
                .build();
        Booking requestForFutureBooking = Booking.builder()
                .booker(userServiceImpl.findById(bookerId))
                .item(itemServiceImpl.findById(itemId))
                .start(Instant.parse("2023-01-01T10:10:10.00Z"))
                .end(Instant.parse("2023-12-30T10:10:10.00Z"))
                .status(BookingStatus.REJECTED)
                .build();
        Booking pastBooking = bookingRepository.save(requestForPastBooking);
        Booking currentBooking = bookingRepository.save(requestForCurrentBooking);
        Booking futureBooking = bookingRepository.save(requestForFutureBooking);
        List<BookingResponseDto> allBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.ALL.toString(), 0, 10);
        List<BookingResponseDto> pastBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.PAST.toString(), 0, 10);
        List<BookingResponseDto> futureBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.FUTURE.toString(), 0, 10);
        List<BookingResponseDto> waitingBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.WAITING.toString(), 0, 10);
        List<BookingResponseDto> rejectedBookings = bookingService.getAllBookingsByBookerId(bookerId,
                RequestState.REJECTED.toString(), 0, 10);
        Assertions.assertEquals(3, allBookings.size());
        Assertions.assertEquals(1, pastBookings.size());
        Assertions.assertEquals(1, futureBookings.size());
        Assertions.assertEquals(2, waitingBookings.size());
        Assertions.assertEquals(1, rejectedBookings.size());
        bookingRepository.deleteAllById(List.of(pastBooking.getId(), currentBooking.getId(), futureBooking.getId()));
    }

    @Test
    void getAllBookingsByOwnerItems() {
        Long itemId = 4L;
        Long itemOwnerId = 4L;
        Long bookerId = 2L;
        Booking requestForPastBooking = Booking.builder()
                .booker(userServiceImpl.findById(bookerId))
                .item(itemServiceImpl.findById(itemId))
                .start(Instant.parse("2021-01-01T10:10:10.00Z"))
                .end(Instant.parse("2021-12-30T10:10:10.00Z"))
                .status(BookingStatus.WAITING)
                .build();
        Booking requestForCurrentBooking = Booking.builder()
                .booker(userServiceImpl.findById(bookerId))
                .item(itemServiceImpl.findById(itemId))
                .start(Instant.parse("2022-01-01T10:10:10.00Z"))
                .end(Instant.parse("2022-12-30T10:10:10.00Z"))
                .status(BookingStatus.WAITING)
                .build();
        Booking requestForFutureBooking = Booking.builder()
                .booker(userServiceImpl.findById(bookerId))
                .item(itemServiceImpl.findById(itemId))
                .start(Instant.parse("2023-01-01T10:10:10.00Z"))
                .end(Instant.parse("2023-12-30T10:10:10.00Z"))
                .status(BookingStatus.REJECTED)
                .build();
        Booking pastBooking = bookingRepository.save(requestForPastBooking);
        Booking currentBooking = bookingRepository.save(requestForCurrentBooking);
        Booking futureBooking = bookingRepository.save(requestForFutureBooking);
        List<BookingResponseDto> allBookings = bookingService.getAllBookingsByOwnerItems(bookerId,
                RequestState.ALL.toString(), 0, 10);
        List<BookingResponseDto> pastBookings = bookingService.getAllBookingsByOwnerItems(bookerId,
                RequestState.PAST.toString(), 0, 10);
        List<BookingResponseDto> futureBookings = bookingService.getAllBookingsByOwnerItems(bookerId,
                RequestState.FUTURE.toString(), 0, 10);
        List<BookingResponseDto> waitingBookings = bookingService.getAllBookingsByOwnerItems(bookerId,
                RequestState.WAITING.toString(), 0, 10);
        List<BookingResponseDto> rejectedBookings = bookingService.getAllBookingsByOwnerItems(bookerId,
                RequestState.REJECTED.toString(), 0, 10);
        Assertions.assertEquals(3, allBookings.size());
        Assertions.assertEquals(1, pastBookings.size());
        Assertions.assertEquals(1, futureBookings.size());
        Assertions.assertEquals(2, waitingBookings.size());
        Assertions.assertEquals(1, rejectedBookings.size());
        bookingRepository.deleteAllById(List.of(pastBooking.getId(), currentBooking.getId(), futureBooking.getId()));
    }
}
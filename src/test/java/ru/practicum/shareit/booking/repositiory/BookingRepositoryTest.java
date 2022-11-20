package ru.practicum.shareit.booking.repositiory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.Instant;
import java.util.List;

@DataJpaTest(properties = "spring.sql.init.data-locations=data-test.sql")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    TestEntityManager em;

    @Test
    void findAllByBookerId() {
        Long bookerId = 1L;
        int numberOfBookings = 4;
        Page<Booking> oneBookingPage = bookingRepository.findAllByBookerId(
                PageRequest.of(0, 1, Sort.by("id")), bookerId);
        Page<Booking> tenBookingPage = bookingRepository.findAllByBookerId(
                PageRequest.of(0, 10, Sort.by("id")), bookerId);
        Assertions.assertEquals(1, oneBookingPage.getNumberOfElements());
        Assertions.assertEquals(numberOfBookings, tenBookingPage.getNumberOfElements());
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter() {
        Long bookerId = 1L;
        int numberOfBookings = 1;
        Long correctBookingId = 3L;
        Instant currentTime = Instant.now();
        Page<Booking> page = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                PageRequest.of(0, 10, Sort.by("id")), bookerId, currentTime, currentTime);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
        Assertions.assertEquals(correctBookingId, page.toList().get(0).getId());
    }

    @Test
    void findAllByBookerIdAndStartAfter() {
        Long bookerId = 1L;
        int numberOfBookings = 1;
        Long correctBookingId = 5L;
        Instant currentTime = Instant.now();
        Page<Booking> page = bookingRepository.findAllByBookerIdAndStartAfter(
                PageRequest.of(0, 10, Sort.by("id")), bookerId, currentTime);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
        Assertions.assertEquals(correctBookingId, page.toList().get(0).getId());
    }

    @Test
    void findAllByBookerIdAndEndBefore() {
        Long bookerId = 1L;
        int numberOfBookings = 2;
        Long correctBookingId = 1L;
        Instant currentTime = Instant.now();
        Page<Booking> page = bookingRepository.findAllByBookerIdAndEndBefore(
                PageRequest.of(0, 10, Sort.by("id")), bookerId, currentTime);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
        Assertions.assertEquals(correctBookingId, page.toList().get(0).getId());
    }

    @Test
    void findAllByBookerIdAndStatus() {
        Long bookerId = 1L;
        int numberOfBookings = 2;
        BookingStatus status = BookingStatus.WAITING;
        Page<Booking> page = bookingRepository.findAllByBookerIdAndStatus(
                PageRequest.of(0, 10, Sort.by("id")), bookerId, status);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
    }

    @Test
    void findAllByItemOwner() {
        Long itemOwner = 1L;
        int numberOfBookings = 4;
        Page<Booking> oneBookingPage = bookingRepository.findAllByItemOwner(
                PageRequest.of(0, 1, Sort.by("id")), itemOwner);
        Page<Booking> tenBookingPage = bookingRepository.findAllByBookerId(
                PageRequest.of(0, 10, Sort.by("id")), itemOwner);
        Assertions.assertEquals(1, oneBookingPage.getNumberOfElements());
        Assertions.assertEquals(numberOfBookings, tenBookingPage.getNumberOfElements());
    }

    @Test
    void findAllByItemOwnerAndStartLessThanAndEndGreaterThan() {
        Long itemOwner = 1L;
        int numberOfBookings = 1;
        Long correctBookingId = 4L;
        Instant currentTime = Instant.now();
        Page<Booking> page = bookingRepository.findAllByItemOwnerAndStartLessThanAndEndGreaterThan(
                PageRequest.of(0, 10, Sort.by("id")), itemOwner, currentTime, currentTime);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
        Assertions.assertEquals(correctBookingId, page.toList().get(0).getId());
    }

    @Test
    void findAllByItemOwnerAndStartGreaterThan() {
        Long itemOwner = 1L;
        int numberOfBookings = 1;
        Long correctBookingId = 6L;
        Instant currentTime = Instant.now();
        Page<Booking> page = bookingRepository.findAllByItemOwnerAndStartGreaterThan(
                PageRequest.of(0, 10, Sort.by("id")), itemOwner, currentTime);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
        Assertions.assertEquals(correctBookingId, page.toList().get(0).getId());
    }

    @Test
    void findAllByItemOwnerAndEndLessThan() {
        Long itemOwner = 1L;
        int numberOfBookings = 2;
        Long correctBookingId = 2L;
        Instant currentTime = Instant.now();
        Page<Booking> page = bookingRepository.findAllByItemOwnerAndEndLessThan(
                PageRequest.of(0, 10, Sort.by("id")), itemOwner, currentTime);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
        Assertions.assertEquals(correctBookingId, page.toList().get(0).getId());
    }

    @Test
    void findAllByItemOwnerAndStatus() {
        Long itemOwner = 1L;
        int numberOfBookings = 2;
        BookingStatus status = BookingStatus.WAITING;
        Page<Booking> page = bookingRepository.findAllByBookerIdAndStatus(
                PageRequest.of(0, 10, Sort.by("id")), itemOwner, status);
        Assertions.assertEquals(numberOfBookings, page.getNumberOfElements());
    }

    @Test
    void findAllByItemIdAndEndBeforeOrderByEndDesc() {
        Long itemId = 3L;
        int numberOfBookings = 2;
        Long correctBookingId = 1L;
        Instant currentTime = Instant.now();
        List<Booking> result = bookingRepository.findAllByItemIdAndEndBeforeOrderByEndDesc(itemId, currentTime);
        Assertions.assertEquals(numberOfBookings, result.size());
        Assertions.assertEquals(correctBookingId, result.get(0).getId());
    }

    @Test
    void findAllByItemIdAndEndAfterOrderByEndDesc() {
        Long itemId = 3L;
        int numberOfBookings = 2;
        Long correctBookingId = 5L;
        Instant currentTime = Instant.now();
        List<Booking> result = bookingRepository.findAllByItemIdAndEndAfterOrderByEndDesc(itemId, currentTime);
        Assertions.assertEquals(numberOfBookings, result.size());
        Assertions.assertEquals(correctBookingId, result.get(0).getId());
    }

    @Test
    void findAllByBookerIdAndItemIdAndEndIsBefore() {
        Long bookerId = 1L;
        Long itemId = 3L;
        BookingStatus statusRejected = BookingStatus.REJECTED;
        BookingStatus statusApproved = BookingStatus.APPROVED;
        BookingStatus statusWaiting = BookingStatus.WAITING;
        int numberOfBookings = 1;
        Long correctBookingId = 7L;
        Instant currentTime = Instant.now();
        List<Booking> result = bookingRepository.findAllByBookerIdAndItemIdAndEndIsBeforeAndStatus(bookerId, itemId,
                currentTime, statusRejected);
        Assertions.assertEquals(numberOfBookings, result.size());
        Assertions.assertEquals(correctBookingId, result.get(0).getId());

        correctBookingId = 1L;
        result = bookingRepository.findAllByBookerIdAndItemIdAndEndIsBeforeAndStatus(bookerId, itemId,
                currentTime, statusWaiting);
        Assertions.assertEquals(numberOfBookings, result.size());
        Assertions.assertEquals(correctBookingId, result.get(0).getId());
//TODO
        result = bookingRepository.findAllByBookerIdAndItemIdAndEndIsBeforeAndStatus(bookerId, itemId,
                currentTime, statusApproved);
        Assertions.assertEquals(0, result.size());

    }

    @Test
    void findByItemIdAndEndAfterAndStartBeforeOrderByStartDesc() {
        Long itemId = 3L;
        Long correctBookingId = 3L;
        Instant currentTime = Instant.now();
        Booking booking = bookingRepository.findByItemIdAndEndAfterAndStartBeforeOrderByStartDesc(itemId, currentTime,
                currentTime).get();
        Assertions.assertEquals(correctBookingId, booking.getId());
    }
}
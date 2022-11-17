package ru.practicum.shareit.booking.repositiory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerId(Pageable pageable, Long userId);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Pageable pageable, Long bookerId, Instant currentTime1,
                                                             Instant currentTime2);

    Page<Booking> findAllByBookerIdAndStartAfter(Pageable pageable, Long userId, Instant currentTime);

    Page<Booking> findAllByBookerIdAndEndBefore(Pageable pageable, Long userId, Instant currentTime);

    Page<Booking> findAllByBookerIdAndStatus(Pageable pageable, Long userId, BookingStatus state);

    Page<Booking> findAllByItemOwner(Pageable pageable, Long userId);

    Page<Booking> findAllByItemOwnerAndStartLessThanAndEndGreaterThan(Pageable pageable, Long userId,
                                                                      Instant currentTime, Instant currentTime2);

    Page<Booking> findAllByItemOwnerAndStartGreaterThan(Pageable pageable, Long userId, Instant currentTime);

    Page<Booking> findAllByItemOwnerAndEndLessThan(Pageable pageable, Long userId, Instant currentTime);

    Page<Booking> findAllByItemOwnerAndStatus(Pageable pageable, Long userId, BookingStatus state);

    List<Booking> findAllByItemIdAndEndBeforeOrderByEndDesc(Long itemId, Instant currentTime);

    List<Booking> findAllByItemIdAndEndAfterOrderByEndDesc(Long itemId, Instant currentTime);

    List<Booking> findAllByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, Instant currentTime);

    Optional<Booking> findByItemIdAndEndAfterAndStartBeforeOrderByStartDesc(Long itemId, Instant start, Instant end);
}

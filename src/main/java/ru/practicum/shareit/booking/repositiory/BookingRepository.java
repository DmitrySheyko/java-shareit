package ru.practicum.shareit.booking.repositiory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(Pageable pageable, Long userId);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Pageable pageable, Long bookerId, Instant currentTime1, Instant currentTime2);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Pageable pageable, Long userId, Instant currentTime);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Pageable pageable, Long userId, Instant currentTime);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Pageable pageable, Long userId, BookingStatus state);

    @Query(" select b " +
            "from Booking b left join Item i on b.item = i.id " +
            "where i.owner = ?1 " +
            "order by b.start desc ")
    Page<Booking> findAllBookingsByItemOwner(Pageable pageable, Long userId);

    @Query(" select b " +
            "from Booking b left join Item i on b.item = i.id " +
            "where i.owner = ?1 and " +
            "b.start < ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    Page<Booking> findAllCurrentBookingsByItemOwner(Pageable pageable, Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.item = i.id " +
            "where i.owner = ?1 and " +
            "b.start > ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    Page<Booking> findAllFutureBookingsByItemOwner(Pageable pageable, Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.item = i.id " +
            "where i.owner = ?1 and " +
            "b.start < ?2  and " +
            "b.end < ?2 " +
            "order by b.start desc ")
    Page<Booking> findAllPastBookingsByItemOwner(Pageable pageable, Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.item = i.id " +
            "where i.owner = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    Page<Booking> findAllWaitingBookingsByItemOwner(Pageable pageable, Long userId, BookingStatus state);

    @Query(" select b " +
            "from Booking b left join Item i on b.item = i.id " +
            "where i.owner = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    Page<Booking> findAllRejectedBookingsByItemOwner(Pageable pageable, Long userId, BookingStatus state);

    List<Booking> findAllByItemIdAndEndBeforeOrderByEndDesc(Long itemId, Instant currentTime);

    List<Booking> findAllByItemIdAndEndAfterOrderByEndDesc(Long itemId, Instant currentTime);

    List<Booking> findAllByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, Instant currentTime);

    Optional<Booking> findByItemIdAndEndAfterAndStartBeforeOrderByStartDesc(Long itemId, Instant start, Instant end);
}

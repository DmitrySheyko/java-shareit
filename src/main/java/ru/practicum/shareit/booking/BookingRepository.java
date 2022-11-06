package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    @Query(" select b from Booking b " +
            "where b.bookerId = ?1 and " +
            "b.start < ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    List<Booking> findAllCurrentByBookerId(Long userId, Instant currentTime);

    @Query(" select b from Booking b " +
            "where b.bookerId = ?1 and " +
            "b.start > ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    List<Booking> findAllFutureByBookerId(Long userId, Instant currentTime);

    @Query(" select b from Booking b " +
            "where b.bookerId = ?1 and " +
            "b.start < ?2  and " +
            "b.end < ?2 " +
            "order by b.start desc ")
    List<Booking> findAllPastByBookerId(Long userId, Instant currentTime);

    @Query(" select b from Booking b " +
            "where b.bookerId = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllWaitingByBookerId(Long userId, BookingStatus state);

    @Query(" select b from Booking b " +
            "where b.bookerId = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllRejectedByBookerId(Long userId, BookingStatus state);


    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 " +
            "order by b.start desc ")
    List<Booking> findAllBookingsByItemOwner(Long userId);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.start < ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    List<Booking> findAllCurrentBookingsByItemOwner(Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.start > ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    List<Booking> findAllFutureBookingsByItemOwner(Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.start < ?2  and " +
            "b.end < ?2 " +
            "order by b.start desc ")
    List<Booking> findAllPastBookingsByItemOwner(Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllWaitingBookingsByItemOwner(Long userId, BookingStatus state);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllRejectedBookingsByItemOwner(Long userId, BookingStatus state);

    @Query(" select b " +
            "from Booking b " +
            "where b.itemId = ?1 and " +
            "b.end < ?2 " +
            "order by b.end desc ")
    List<Booking> findLastBookingsByItemId(Long itemId, Instant currentTime);

    @Query(" select b " +
            "from Booking b " +
            "where b.itemId = ?1 and " +
            "b.start > ?2 " +
            "order by b.start asc ")
    List<Booking> findNextBookingsByItemId(Long itemId, Instant currentTime);

    List<Booking> findAllByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, Instant currentTime);
}

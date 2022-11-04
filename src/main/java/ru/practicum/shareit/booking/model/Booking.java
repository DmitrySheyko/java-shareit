package ru.practicum.shareit.booking.model;

import lombok.*;
import org.aspectj.lang.annotation.After;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Future(message = "Некоректная дата начала бронирования")
    @Column(name = "start_date", nullable = false)
    private Instant start;

    @Future(message = "Некоректная дата окончания бронирования")
    @Column(name = "end_date", nullable = false)
    private Instant end;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "booker_id", nullable = false)
    private Long bookerId;

    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(start, booking.start)
                && Objects.equals(end, booking.end)
                && Objects.equals(itemId, booking.itemId)
                && Objects.equals(bookerId, booking.bookerId)
                && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, itemId, bookerId, status);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", itemId=" + itemId +
                ", bookerId=" + bookerId +
                ", status=" + status +
                '}';
    }
}

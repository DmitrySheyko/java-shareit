package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @OneToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (!id.equals(booking.id)) return false;
        if (!start.equals(booking.start)) return false;
        if (!end.equals(booking.end)) return false;
        if (!item.equals(booking.item)) return false;
        if (!booker.equals(booking.booker)) return false;
        return status == booking.status;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + item.hashCode();
        result = 31 * result + booker.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}

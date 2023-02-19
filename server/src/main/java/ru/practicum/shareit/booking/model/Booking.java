package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.Instant;

/**
 * Class of entity {@link Booking}.
 *
 * @author DmitrySheyko
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "start_date", nullable = false)
    private Instant start;

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

}
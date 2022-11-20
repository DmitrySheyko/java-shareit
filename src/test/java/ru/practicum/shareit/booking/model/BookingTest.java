package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

class BookingTest {
    User user = User.builder().id(1L).name("UserName1").email("User1@email.com").build();
    Item item = Item.builder().id(1L).name("Name 1").description("Description 1").owner(1L).available(true)
            .requestId(1L).build();
    Booking booking1 = Booking.builder().id(1L).item(item).booker(user)
            .start(Instant.parse("2020-10-10T10:10:10.00Z")).end(Instant.parse("2020-11-10T10:10:10.00Z"))
            .status(BookingStatus.WAITING).build();
    Booking booking2 = new Booking(2L, Instant.parse("2021-10-10T10:10:10.00Z"),
            Instant.parse("2021-11-10T10:10:10.00Z"), item, user, BookingStatus.WAITING);
    Booking booking3 = booking1;

    @Test
    void testEquals() {
        Assertions.assertEquals(booking1, booking3);
        Assertions.assertNotEquals(booking1, booking2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(booking1.hashCode(), booking3.hashCode());
        Assertions.assertNotEquals(booking1.hashCode(), booking2.hashCode());
    }
}
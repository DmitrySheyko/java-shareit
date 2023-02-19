package ru.practicum.shareit.booking.model;

/**
 * Enumeration class of searching parameters for entity {@link Booking}.
 *
 * @author DmitrySheyko
 */
public enum RequestState {

    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED

}

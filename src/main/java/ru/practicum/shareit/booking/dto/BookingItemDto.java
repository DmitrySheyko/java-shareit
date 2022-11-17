package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingItemDto {

    private Long id;
    private Long bookerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingItemDto that = (BookingItemDto) o;

        if (!id.equals(that.id)) return false;
        return bookerId.equals(that.bookerId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + bookerId.hashCode();
        return result;
    }
}

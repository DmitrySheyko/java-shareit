package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingResponseDtoTest {

    @Autowired
    private JacksonTester<BookingResponseDto> json;

    @Test
    void testBookingResponseDto() throws Exception {
        User user = User.builder().id(1L).name("UserName").email("User@email.com").build();
        Item item = Item.builder().id(2L).name("ItemName").description("Item description").owner(3L).available(true)
                .requestId(4L).build();

        BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
                .id(5L)
                .start("2021-10-10 10:10:10")
                .end("2022-10-10 10:10:10")
                .item(item)
                .booker(user)
                .build();
        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2021-10-10 10:10:10");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-10-10 10:10:10");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("Item description");
        assertThat(result).extractingJsonPathNumberValue("$.item.owner").isEqualTo(3);
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.item.requestId").isEqualTo(4);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("UserName");
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo("User@email.com");
    }
}
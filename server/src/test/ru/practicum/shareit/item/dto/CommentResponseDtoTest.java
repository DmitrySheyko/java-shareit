package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentResponseDtoTest {

    @Autowired
    private JacksonTester<CommentResponseDto> json;

    @Test
    void testCommentResponseDto() throws Exception {
        CommentResponseDto commentResponseDto = CommentResponseDto.builder().id(1L).text("Test text")
                .authorName("Author name").created("2022-10-10 10:10:10").build();

        JsonContent<CommentResponseDto> result = json.write(commentResponseDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Test text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Author name");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2022-10-10 10:10:10");
    }
}
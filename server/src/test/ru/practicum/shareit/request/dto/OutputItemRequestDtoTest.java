package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OutputItemRequestDtoTest {

    @Autowired
    private JacksonTester<OutputItemRequestDto> json;

    @Test
    void testOutputItemRequestDto() throws Exception {
        List<AnswerDto> items = List.of(AnswerDto.builder().id(2L).name("ItemName").description("Item description")
                .available(true).owner(1L).requestId(4L).build());

        OutputItemRequestDto outputItemRequestDto = OutputItemRequestDto.builder()
                .id(1L)
                .description("Test description")
                .created("2022-01-01 10:10:10")
                .items(items)
                .build();

        JsonContent<OutputItemRequestDto> result = json.write(outputItemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Test description");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2022-01-01 10:10:10");
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.items.[0].description")
                .isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.items.[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].requestId").isEqualTo(4);
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].owner").isEqualTo(1);
    }
}
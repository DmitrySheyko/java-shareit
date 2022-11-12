package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class ItemRequestMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ItemRequest inputItemRequestDtoToEntity(InputItemRequestDto inputItemRequestDto) {
        if (inputItemRequestDto == null) {
            return null;
        } else {
            return ItemRequest.builder()
                    .description(inputItemRequestDto.getDescription())
                    .requestor(inputItemRequestDto.getRequestor())
                    .build();
        }
    }

    public OutputItemRequestDto entityToOutputItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        } else {
            return OutputItemRequestDto.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .requestor(itemRequest.getRequestor())
                    .created(instantToString(itemRequest.getCreated()))
                    .build();
        }
    }

    private String instantToString(Instant instantTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instantTime, ZoneId.systemDefault());
        return DATE_TIME_PATTERN.withZone(ZoneId.systemDefault()).format(zonedDateTime);
    }

    private Instant stringToInstant(String stringTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(stringTime, DATE_TIME_PATTERN);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }
}

package ru.practicum.shareit.request.itemRequestMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            OutputItemRequestDto result = new OutputItemRequestDto();
            result.setId(itemRequest.getId());
            result.setDescription(itemRequest.getDescription());
            result.setCreated(instantToString(itemRequest.getCreated()));
            result.setItems(getAnswerDtoList(itemRequest));
            return result;
//            return OutputItemRequestDto.builder()
//                    .description(itemRequest.getDescription())
//                    .created(instantToString(itemRequest.getCreated()))
//                    .answersList(itemRequest.getAnswersList().stream()
//                            .map(this::itemToAnswerDto)
//                            .collect(Collectors.toList()))
//                    .build();
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

    private AnswerDto itemToAnswerDto(Item item) {
        if (item == null) {
            return null;
        } else {
            return AnswerDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .requestId(item.getRequestId())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner())
                    .build();
        }
    }

    private List<AnswerDto> getAnswerDtoList(ItemRequest itemRequest) {
        if (itemRequest.getAnswersList() == null) {
            return Collections.emptyList();
        } else {
            return itemRequest.getAnswersList().stream()
                    .map(this::itemToAnswerDto)
                    .collect(Collectors.toList());
        }
    }
}

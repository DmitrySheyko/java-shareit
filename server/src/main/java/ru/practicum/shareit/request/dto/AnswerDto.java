package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private String name;
    private String description;
    private Long requestId;
    private Boolean available;
    private Long owner;
}

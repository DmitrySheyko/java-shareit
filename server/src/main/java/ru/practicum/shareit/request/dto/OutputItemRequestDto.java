package ru.practicum.shareit.request.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputItemRequestDto {
    private Long id;
    private String description;
    private String created;
    private List<AnswerDto> items;
}

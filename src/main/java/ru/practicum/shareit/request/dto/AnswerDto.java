package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {
    private Long id;
    private String name;
    private String description;
    private Long requestId;
    private Boolean available;
    private Long owner;
}

package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InputItemRequestDto {
    private String description;
    private Long requestor;
}

package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InputItemRequestDto {
    @NotBlank(message = "Описание заявки не должно быть пустым")
    private String description;
    private Long requestor;
}

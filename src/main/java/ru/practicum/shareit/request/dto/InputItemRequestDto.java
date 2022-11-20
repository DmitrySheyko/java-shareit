package ru.practicum.shareit.request.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class InputItemRequestDto {
    @NotBlank(message = "Описание заявки не должно быть пустым")
    private String description;
    private Long requestor;

    @Override
    public String toString() {
        return "InputItemRequestDto{" +
                "description='" + description + '\'' +
                ", requestor=" + requestor +
                '}';
    }
}

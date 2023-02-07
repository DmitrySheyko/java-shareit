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
    @NotBlank
    private String description;
    private Long requestor;

    @Override
    public String toString() {
        return "ItemRequest {" +
                "description='" + description + '\'' +
                ", requestor=" + requestor +
                '}';
    }
}

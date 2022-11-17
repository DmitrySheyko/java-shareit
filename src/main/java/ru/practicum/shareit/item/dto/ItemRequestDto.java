package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Не указано название")
    private String name;
    @NotBlank(message = "Не указано описание")
    private String description;
    @NotNull(message = "В запросе не предостален id пользователя")
    private Long owner;
    @NotNull
    private Boolean available;
    private Long requestId;
}


package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ItemRequestDto {
    @Positive(message = "Указан некорректный Id объекта")
    private Long id;
    @NotBlank(message = "Указано некорректное имя объекта")
    private String name;
    @NotBlank(message = "Указано некорректное описание объекта")
    private String description;
    @Positive(message = "Указан некорректный Id владельца объекта")
    private Long owner;
    @NotNull(message = "Не указан статус доступности объекта")
    private Boolean available;
    @Positive(message = "Указан некорректный Id запроса на основе которого добавлен объект")
    private Long requestId;

    @Override
    public String toString() {
        return "Item {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", available=" + available +
                ", requestId=" + requestId +
                '}';
    }

    public void checkName(@Valid String name) {
        this.name = name;
    }

    public void checkDescription(@Valid String description) {
        this.description = description;
    }

    public void checkRequestDto(ItemRequestDto requestDto) {
        if (requestDto.getName() == null && requestDto.getDescription() == null && requestDto.getAvailable() == null) {
            throw new ValidationException(String.format("Предоставлено недостаточно информации для обновления данных " +
                    "об объекте id=%s", requestDto.getId()));
        }
    }
}
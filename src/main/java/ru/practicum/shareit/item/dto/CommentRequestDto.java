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
public class CommentRequestDto {
    private Long id;
    @NotBlank(message = "Текст комментария не должен быть пустым")
    private String text;
    @NotNull(message = "В запросе не предостален id объекта")
    private Long item;
    @NotNull(message = "В запросе не предостален id пользователя")
    private Long author;
}

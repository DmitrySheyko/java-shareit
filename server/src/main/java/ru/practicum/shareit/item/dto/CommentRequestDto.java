package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;
    private String text;
    private Long item;
    private Long author;
}
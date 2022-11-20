package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String text;
    private String authorName;
    private String created;
}


package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;
    @NotBlank
    private String text;
    private Long item;
    private Long author;
}
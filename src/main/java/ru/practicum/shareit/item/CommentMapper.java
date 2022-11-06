package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class CommentMapper {
    private final static DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final UserService userService;

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        } else {
            return CommentResponseDto.builder()
                    .id(comment.getId())
                    .authorName(userService.findById(comment.getAuthor()).getName())
                    .text(comment.getText())
                    .created(instantToString(comment.getCreated()))
                    .build();
        }
    }

    public Comment toEntity(CommentRequestDto commentRequestDto) {
        if (commentRequestDto == null) {
            return null;
        } else {
            return Comment.builder()
                    .id(commentRequestDto.getId())
                    .author(commentRequestDto.getAuthor())
                    .item(commentRequestDto.getItem())
                    .text(commentRequestDto.getText())
                    .build();
        }
    }

    private String instantToString(Instant instantTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instantTime, ZoneId.systemDefault());
        return DATE_TIME_PATTERN.withZone(ZoneId.systemDefault()).format(zonedDateTime);
    }
}

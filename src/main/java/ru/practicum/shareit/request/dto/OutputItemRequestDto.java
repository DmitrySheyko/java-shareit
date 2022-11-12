package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputItemRequestDto {
        private Long id;
        private String description;
        private Long requestor;
        private String created;
}

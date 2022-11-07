package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "item_id", nullable = false)
    private Long item;

    @Column(name = "author_id", nullable = false)
    private Long author;

    @Column(name = "created", nullable = false)
    private Instant created;
}


package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

/**
 * Class of entity {@link Comment}.
 *
 * @author DmitrySheyko
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "comment_text", nullable = false, length = 500)
    private String text;

    @Column(name = "item_id", nullable = false)
    private Long item;

    @Column(name = "author_id", nullable = false)
    private Long author;

    @Column(name = "created", nullable = false)
    private Instant created;

}


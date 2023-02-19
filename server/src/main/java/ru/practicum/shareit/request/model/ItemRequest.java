package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

/**
 * Class of entity {@link ItemRequest}.
 *
 * @author DmitrySheyko.
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "requestor_id")
    private Long requestor;

    @Column(name = "created")
    private Instant created;

    @OneToMany
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private List<Item> answersList;

}

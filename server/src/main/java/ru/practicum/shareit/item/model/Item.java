package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;

/**
 * Class of entity {@link Item}.
 *
 * @author DmitrySheyko
 */
@Entity
@Table(name = "items")
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private Long owner;

    @Column(name = "request_id")
    private Long requestId;

}

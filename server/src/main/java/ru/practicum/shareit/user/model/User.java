package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * Class of entity {@link User}
 *
 * @author DmitrySheyko
 */
@Entity
@Table(name = "users")
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

}
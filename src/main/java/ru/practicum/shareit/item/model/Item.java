package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    @Column(name = "is_available")
    @NotNull(message = "Должно быть указано доступен ли объект: true/false")
    private Boolean available;

    @NotNull(message = "Должен быть указан UserId владельца")
    private Long owner;

    @Transient
    private ItemRequest request;
}

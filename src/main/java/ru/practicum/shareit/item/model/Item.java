package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "owner_id")
    @NotNull(message = "Должен быть указан UserId владельца")
    private Long owner;

    @Column(name = "request_id")
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

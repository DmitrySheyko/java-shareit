package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    @NotNull (message = "В описании пользователя отсутвует email")
    @Email (message = "В описании пользователя представлен некорректный email")
    private String email;
}

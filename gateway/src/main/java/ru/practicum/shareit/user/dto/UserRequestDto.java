package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class UserRequestDto {
    @Positive(message = "Указан некорректный Id пользователя")
    private Long id;
    @NotNull
    @NotBlank(message = "Не указано имя пользователя")
    @Size(min = 1, max = 255, message = "Указано некорректное имя пользователя")
    private String name;
    @NotNull
    @Email(message = "Указан некорректный email пользователя")
    private String email;

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void checkName(@Valid String name) {
        this.name = name;
    }

    public void checkEmail(@Valid String email) {
        this.email = email;
    }
}
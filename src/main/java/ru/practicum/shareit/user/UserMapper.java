package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.interfaces.Mappers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

@Component
@Validated
public class UserMapper implements Mappers<UserDto, User> {

    @Override
    public UserDto toDtoForOtherUsers(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Valid
    public User DtoForOtherUsersToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}

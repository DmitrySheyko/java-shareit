package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto add(User user);

    UserDto update(Long userId, User user);

    UserDto getById(Long userId);

    List<UserDto> getAll();

    String delete(Long userId);
}

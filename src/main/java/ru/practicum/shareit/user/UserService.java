package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    UserDto add(User user);

    UserDto update(Long userId, User user);

    UserDto getById(Long userId);

    List<UserDto> getAll();
}

package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(Long userId, User user);

    User getById(Long userId);

    List<User> getAll();
}

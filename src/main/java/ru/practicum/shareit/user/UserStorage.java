package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(Long userId, User user);

    User getById(Long userId);

    List<User> getAll();

    Boolean checkIsUserInStorage(Long userId);

    Boolean checkIsUserInStorage(User user);

    Boolean checkIsUserEmailInStorage(User user);

    String delete (Long userId);
}

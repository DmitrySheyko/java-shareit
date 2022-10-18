package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class userServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto add(User user) {
        if (!userStorage.checkIsUserEmailInStorage(user)) {
            System.out.println("Service");
            User newUser = userStorage.add(user);
            log.info(String.format("Пользователь id=%s успешно добавлен.", user.getId()));
            return userMapper.toUserDto(newUser);
        } else {
            log.warn(String.format("Пользователь с email=%s уже существует.", user.getEmail()));
            throw new ValidationException(String.format("Пользователь с email=%s уже существует.", user.getEmail()));
        }
    }

    @Override
    public UserDto update(Long userId, User userForUpdate) {
        if (userStorage.checkIsUserInStorage(userId)) {
            User userFromStorage = userStorage.getById(userId);
            if (userForUpdate.getName() == null) {
                userForUpdate.setName(userFromStorage.getName());
            }
            if (userStorage.checkIsUserEmailInStorage(userForUpdate)) {
                log.warn(String.format("Пользователь с email=%s уже существует.", userForUpdate.getEmail()));
                throw new ValidationException(String.format("Пользователь с email=%s уже существует.", userForUpdate.getEmail()));
            }
            if (userForUpdate.getEmail() == null) {
                userForUpdate.setEmail(userFromStorage.getEmail());
            }
            User updatedUser = userStorage.update(userId, userForUpdate);
            log.info(String.format("Пользователь id=%s успешно обновлен.", userId));
            return userMapper.toUserDto(updatedUser);
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    @Override
    public UserDto getById(Long userId) throws ObjectNotFoundException {
        if (userStorage.checkIsUserInStorage(userId)) {
            User user = userStorage.getById(userId);
            log.info(String.format("Получены данные пользователя id=%s.", userId));
            return userMapper.toUserDto(user);
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    @Override
    public List<UserDto> getAll() {
        List<User> listOfUsers = userStorage.getAll();
        log.info("Получены список всех пользователей");
        return listOfUsers.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    public String delete(Long userId) {
        if (userStorage.checkIsUserInStorage(userId)) {
            String message = userStorage.delete(userId);
            log.info(message);
            return message;
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }
}

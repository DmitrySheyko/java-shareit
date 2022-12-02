package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto add(UserDto userDto) {
        User user = userMapper.userDtoToEntity(userDto);
        User createdUser = userRepository.save(user);
        log.info(String.format("Пользователь id=%s успешно добавлен.", createdUser.getId()));
        return userMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto update(UserDto userDtoForUpdate) {
        checkIsObjectInStorage(userDtoForUpdate.getId());
        User userFromStorage = findById(userDtoForUpdate.getId());
        if (userDtoForUpdate.getEmail() == null) {
            userDtoForUpdate.setEmail(userFromStorage.getEmail());
        }
        userDtoForUpdate.setName(Optional.ofNullable(userDtoForUpdate.getName()).orElse(userFromStorage.getName()));
        User userForUpdate = userMapper.userDtoToEntity(userDtoForUpdate);
        User updatedUser = userRepository.save(userForUpdate);
        log.info(String.format("Пользователь id=%s успешно обновлен.", userDtoForUpdate.getId()));
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getById(Long userId) {
        checkIsObjectInStorage(userId);
        User user = findById(userId);
        UserDto result = userMapper.toUserDto(user);
        log.info(String.format("Получены данные пользователя id=%s.", userId));
        return result;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> listOfUsers = userRepository.findAll();
        log.info("Получен список всех пользователей");
        return listOfUsers.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> delete(Long userId) {
        checkIsObjectInStorage(userId);
        userRepository.deleteById(userId);
        log.info(String.format("Пользователь id=%s успешно удален", userId));
        return Map.of("Успешно удален пользователь id=", userId);
    }

    @Override
    public void checkIsObjectInStorage(Long userId) {
        if (!(userId != null && userRepository.existsById(userId))) {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    @Override
    public User findById(Long userId) {
        if (userId == null) {
            log.warn(String.format("Данные пользователя id=%s не найдены.", userId));
            throw new ObjectNotFoundException(String.format("Данные пользователя id=%s не найдены.", userId));
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.info(String.format("Получены данные пользователя id=%s.", userId));
            return user;
        } else {
            log.warn(String.format("Данные пользователя id=%s не найдены.", userId));
            throw new ObjectNotFoundException(String.format("Данные пользователя id=%s не найдены.", userId));
        }
    }
}

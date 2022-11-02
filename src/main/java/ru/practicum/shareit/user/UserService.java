package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictErrorException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.interfaces.Mappers;
import ru.practicum.shareit.interfaces.Services;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
@Slf4j
public class UserService implements Services<UserDto> {

    private final UserRepository userRepository;
    private final Mappers<UserDto, User> userMapper;

    @Autowired
    public UserService(UserRepository userRepository, Mappers<UserDto, User> userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto add(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
//        if (userRepository.checkIsUserEmailInStorage(user.getEmail())) {
//            log.warn(String.format("Пользователь с email=%s уже существует.", user.getEmail()));
//            throw new ConflictErrorException(String.format("Пользователь с email=%s уже существует.", user.getEmail()));
//        }
        User createdUser = userRepository.save(user);
        log.info(String.format("Пользователь id=%s успешно добавлен.", userDto.getId()));
        return userMapper.toDto(createdUser);
    }

    @Override
    public UserDto update(UserDto userDtoForUpdate) {
        if (!userRepository.existsById(userDtoForUpdate.getId())) {
            log.warn(String.format("Пользователь id=%s не найден.", userDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userDtoForUpdate.getId()));
        }
        User userFromStorage = userRepository.findById(userDtoForUpdate.getId()).get();

        if (userDtoForUpdate.getEmail() == null) {
            userDtoForUpdate.setEmail(userFromStorage.getEmail());
        }
        userDtoForUpdate.setName(Optional.ofNullable(userDtoForUpdate.getName()).orElse(userFromStorage.getName()));
        User userForUpdate = userMapper.toEntity(userDtoForUpdate);
        User updatedUser = userRepository.save(userForUpdate); //  update(userForUpdate);
        log.info(String.format("Пользователь id=%s успешно обновлен.", userDtoForUpdate.getId()));
        return userMapper.toDto(updatedUser);
    }

    @Override
    public UserDto getById(Long userId) throws ObjectNotFoundException {
//        if (userRepository.existsById(userId)) {
            User user = userRepository.findById(userId).get();
            log.info(String.format("Получены данные пользователя id=%s.", userId));
            return userMapper.toDto(user);
//        } else {
//            log.warn(String.format("Пользователь id=%s не найден.", userId));
//            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
//        }
    }

    @Override
    public List<UserDto> getAll() {
        List<User> listOfUsers = userRepository.findAll();
        log.info("Получены список всех пользователей");
        return listOfUsers.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public String delete(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            log.info(String.format("Пользователь id=%s успешно удален", userId));
            return String.format("Пользователь id=%s успешно удален", userId);
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }
}

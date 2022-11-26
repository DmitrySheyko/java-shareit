package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserServiceImpl userService;
    private final String name = "TestName";
    private final String email = "Test@email.com";
    private final String nameForUpdate = "Updated Name";
    private final String emailForUpdate = "Updated@email.com";
    private UserDto inputUserDto;

    @BeforeEach
    void init() {
        inputUserDto = UserDto.builder().name(name).email(email).build();
    }

    @Test
    void add() {
        UserDto outputUserDto = userService.add(inputUserDto);
        Assertions.assertNotNull(outputUserDto.getId());
        Assertions.assertEquals(inputUserDto.getName(), outputUserDto.getName());
        Assertions.assertEquals(inputUserDto.getEmail(), outputUserDto.getEmail());
    }

    @Test
    void update() {
        UserDto outputUserDto = userService.add(inputUserDto);
        Long userId = outputUserDto.getId();
        UserDto userDtoForUpdate = UserDto.builder().id(userId).name(nameForUpdate).email(emailForUpdate).build();
        UserDto updatedUserDto = userService.update(userDtoForUpdate);
        Assertions.assertEquals(userId, updatedUserDto.getId());
        Assertions.assertEquals(nameForUpdate, updatedUserDto.getName());
        Assertions.assertEquals(emailForUpdate, updatedUserDto.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNullUserId() {
        Long userId = null;
        UserDto userDtoForUpdate = UserDto.builder().id(userId).name(nameForUpdate).email(emailForUpdate).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.update(userDtoForUpdate));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", userId), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithIncorrectUserId() {
        Long userId = 100L;
        UserDto userDtoForUpdate = UserDto.builder().id(userId).name(nameForUpdate).email(emailForUpdate).build();
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.update(userDtoForUpdate));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", userId), thrown.getMessage());
    }

    @Test
    void getById() {
        Long userId = 2L;
        String userName = "User2";
        String userEmail = "User2@email.com";
        UserDto userFromBd = userService.getById(userId);
        Assertions.assertEquals(userId, userFromBd.getId());
        Assertions.assertEquals(userName, userFromBd.getName());
        Assertions.assertEquals(userEmail, userFromBd.getEmail());

        Long notExistsUserId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.getById(notExistsUserId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsUserId), thrown.getMessage());

        Long nullUserId = null;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(nullUserId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullUserId), thrown.getMessage());
    }

    @Test
    void getAll() {
        int numberOfUsers = 4;
        Long userId = 1L;
        String userName = "User1";
        String userEmail = "User1@email.com";
        List<UserDto> userDtoList = userService.getAll();
        Assertions.assertEquals(numberOfUsers, userDtoList.size());
        UserDto userFromList = userDtoList.get(0);
        Assertions.assertEquals(userId, userFromList.getId());
        Assertions.assertEquals(userName, userFromList.getName());
        Assertions.assertEquals(userEmail, userFromList.getEmail());
    }

    @Test
    void delete() {
        List<UserDto> userDtoList = userService.getAll();
        int numberOfUsers = userDtoList.size();
        UserDto outputUserDto = userService.add(UserDto.builder().name(name).email(email).build());
        userDtoList = userService.getAll();
        Assertions.assertEquals(numberOfUsers + 1, userDtoList.size());
        userService.delete(outputUserDto.getId());
        userDtoList = userService.getAll();
        Assertions.assertEquals(numberOfUsers, userDtoList.size());

        Long notExistsUserId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.delete(notExistsUserId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsUserId), thrown.getMessage());

        Long nullUserId = null;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.delete(nullUserId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullUserId), thrown.getMessage());
    }

    @Test
    void checkIsUserInStorage() {
        Long existsUserId = 1L;
        Assertions.assertDoesNotThrow(() -> userService.checkIsObjectInStorage(existsUserId));

        Long notExistsUserId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.checkIsObjectInStorage(notExistsUserId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", notExistsUserId), thrown.getMessage());

        Long nullUserId = null;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.checkIsObjectInStorage(nullUserId));
        Assertions.assertEquals(String.format("Пользователь id=%s не найден.", nullUserId), thrown.getMessage());
    }

    @Test
    void findById() {
        Long userId = 1L;
        String userName = "User1";
        String userEmail = "User1@email.com";
        UserDto userFromBd = userService.getById(userId);
        Assertions.assertEquals(userId, userFromBd.getId());
        Assertions.assertEquals(userName, userFromBd.getName());
        Assertions.assertEquals(userEmail, userFromBd.getEmail());

        Long notExistsUserId = 100L;
        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.findById(notExistsUserId));
        Assertions.assertEquals(String.format("Данные пользователя id=%s не найдены.", notExistsUserId),
                thrown.getMessage());

        Long nullUserId = null;
        thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.findById(nullUserId));
        Assertions.assertEquals(String.format("Данные пользователя id=%s не найдены.", nullUserId),
                thrown.getMessage());
    }
}
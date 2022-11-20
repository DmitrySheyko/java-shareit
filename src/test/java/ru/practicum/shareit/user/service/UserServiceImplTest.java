package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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
    final UserServiceImpl userService;

    @Test
    void add() {
        String name = "TestName";
        String email = "Test@amai.com";
        UserDto inputUserDto = UserDto.builder().name(name).email(email).build();
        UserDto outputUserDto = userService.add(inputUserDto);
        Assertions.assertNotNull(outputUserDto.getId());
        Assertions.assertEquals(inputUserDto.getName(), outputUserDto.getName());
        Assertions.assertEquals(inputUserDto.getEmail(), outputUserDto.getEmail());
    }

    @Test
    void update() {
        String name = "TestName";
        String email = "Test@email.com";
        UserDto inputUserDto = UserDto.builder().name(name).email(email).build();
        UserDto outputUserDto = userService.add(inputUserDto);

        Long userId = outputUserDto.getId();
        String nameForUpdate = "Updated Name";
        String emailForUpdate = "Updated@email.com";
        UserDto userDtoForUpdate = UserDto.builder().id(userId).name(nameForUpdate).email(emailForUpdate).build();
        UserDto updatedUserDto = userService.update(userDtoForUpdate);
        Assertions.assertEquals(userId, updatedUserDto.getId());
        Assertions.assertEquals(nameForUpdate, updatedUserDto.getName());
        Assertions.assertEquals(emailForUpdate, updatedUserDto.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNullUserId() {
        Long userId = null;
        String nameForUpdate = "Updated Name";
        String emailForUpdate = "Updated@email.com";
        UserDto userDtoForUpdate = UserDto.builder().id(userId).name(nameForUpdate).email(emailForUpdate).build();
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.update(userDtoForUpdate));
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithIncorrectUserId() {
        Long userId = 100L;
        String nameForUpdate = "Updated Name";
        String emailForUpdate = "Updated@email.com";
        UserDto userDtoForUpdate = UserDto.builder().id(userId).name(nameForUpdate).email(emailForUpdate).build();
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.update(userDtoForUpdate));
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
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(notExistsUserId));

        Long nullUserId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(nullUserId));
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
        String name = "TestName";
        String email = "Test@email.com";
        List<UserDto> userDtoList = userService.getAll();
        int numberOfUsers = userDtoList.size();
        UserDto outputUserDto = userService.add(UserDto.builder().name(name).email(email).build());
        userDtoList = userService.getAll();
        Assertions.assertEquals(numberOfUsers + 1, userDtoList.size());
        userService.delete(outputUserDto.getId());
        userDtoList = userService.getAll();
        Assertions.assertEquals(numberOfUsers, userDtoList.size());

        Long notExistsUserId = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.delete(notExistsUserId));

        Long nullUserId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.delete(nullUserId));
    }

    @Test
    void checkIsUserInStorage() {
        Long existsUserId = 1L;
        Assertions.assertDoesNotThrow(() -> userService.checkIsObjectInStorage(existsUserId));

        Long notExistsUserId = 100L;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.checkIsObjectInStorage(notExistsUserId));

        Long nullUserId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.checkIsObjectInStorage(nullUserId));
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
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.findById(notExistsUserId));

        Long nullUserId = null;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.findById(nullUserId));
    }
}
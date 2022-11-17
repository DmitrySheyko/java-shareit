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
//        properties = "spring.jpa.properties.db.name=test",
        properties = "spring.sql.init.data-locations=data-test.sql",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    final UserServiceImpl userService;

    @Test
    void add() {
        UserDto inputUserDto = UserDto.builder().name("TestName").email("Test@amai.com").build();
        UserDto outputUserDto = userService.add(inputUserDto);
        Assertions.assertNotNull(outputUserDto.getId());
        Assertions.assertEquals(inputUserDto.getName(), outputUserDto.getName());
        Assertions.assertEquals(inputUserDto.getEmail(), outputUserDto.getEmail());
    }

    @Test
    void update() {
        UserDto inputUserDto = UserDto.builder().name("TestName").email("Test@amai.com").build();
        UserDto outputUserDto = userService.add(inputUserDto);
        UserDto userDtoForUpdate = UserDto.builder().id(outputUserDto.getId()).name("UpdatedName").build();
        UserDto updatedUserDto = userService.update(userDtoForUpdate);
        Assertions.assertEquals(userDtoForUpdate.getId(), updatedUserDto.getId());
        Assertions.assertEquals(userDtoForUpdate.getName(), updatedUserDto.getName());
        Assertions.assertEquals(userDtoForUpdate.getEmail(), updatedUserDto.getEmail());
        userDtoForUpdate.setEmail("Updated@amai.com");
        updatedUserDto = userService.update(userDtoForUpdate);
        Assertions.assertEquals(userDtoForUpdate.getId(), updatedUserDto.getId());
        Assertions.assertEquals(userDtoForUpdate.getName(), updatedUserDto.getName());
        Assertions.assertEquals(userDtoForUpdate.getEmail(), updatedUserDto.getEmail());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(100L));
    }

    @Test
    void getById() {
        UserDto userFromBd = userService.getById(2L);
        Assertions.assertEquals(2L, userFromBd.getId());
        Assertions.assertEquals("User2", userFromBd.getName());
        Assertions.assertEquals("User2@email.com", userFromBd.getEmail());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(100L));
    }

    @Test
    void getAll() {
        List<UserDto> userDtoList = userService.getAll();
        Assertions.assertEquals(4, userDtoList.size());
        UserDto userFromList = userDtoList.get(0);
        Assertions.assertEquals(1L, userFromList.getId());
        Assertions.assertEquals("User1", userFromList.getName());
        Assertions.assertEquals("User1@email.com", userFromList.getEmail());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(100L));
    }

    @Test
    void delete() {
        List<UserDto> userDtoList = userService.getAll();
        int initialSize = userDtoList.size();
        UserDto inputUserDto = UserDto.builder().name("TestName").email("Test@amai.com").build();
        UserDto outputUserDto = userService.add(inputUserDto);
        userDtoList = userService.getAll();
        Assertions.assertEquals(initialSize + 1, userDtoList.size());
        userService.delete(outputUserDto.getId());
        userDtoList = userService.getAll();
        Assertions.assertEquals(initialSize, userDtoList.size());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(100L));
    }

    @Test
    void checkIsUserInStorage() {
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.checkIsObjectInStorage(100L));
        Assertions.assertDoesNotThrow(() -> userService.checkIsObjectInStorage(1L));
    }

    @Test
    void findById() {
        UserDto userFromBd = userService.getById(2L);
        Assertions.assertEquals(2L, userFromBd.getId());
        Assertions.assertEquals("User2", userFromBd.getName());
        Assertions.assertEquals("User2@email.com", userFromBd.getEmail());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(100L));
    }
}
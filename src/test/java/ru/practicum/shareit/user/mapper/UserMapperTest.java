package ru.practicum.shareit.user.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserMapperTest {
    private final UserMapper userMapper;
    private final Long id = 1L;
    private final String name = "TestName";
    private final String email = "Test@email.com";

    @Test
    void toUserDto() {
        User user = User.builder().id(id).name(name).email(email).build();
        UserDto userDto = userMapper.toUserDto(user);
        Assertions.assertEquals(userDto.getId(), id);
        Assertions.assertEquals(userDto.getName(), name);
        Assertions.assertEquals(userDto.getEmail(), email);

        userDto = userMapper.toUserDto(null);
        Assertions.assertNull(userDto);
    }

    @Test
    void userDtoToEntity() {
        UserDto userDto = UserDto.builder().id(id).name(name).email(email).build();
        User user = userMapper.userDtoToEntity(userDto);
        Assertions.assertEquals(user.getId(), id);
        Assertions.assertEquals(user.getName(), name);
        Assertions.assertEquals(user.getEmail(), email);

        user = userMapper.userDtoToEntity(null);
        Assertions.assertNull(user);
    }
}
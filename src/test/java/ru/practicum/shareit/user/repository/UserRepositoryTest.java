package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldAddIdWhenSaveNewEntity() {
        User user = User.builder().name("UserName").email("User@mail.com").build();
        Assertions.assertNull(user.getId());
        userRepository.save(user);
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void shouldThrowExceptionWhenSaveSameEmails() {
        User user = User.builder().name("UserName").email("User@mail.com").build();
        User user2 = User.builder().name("UserName").email("User@mail.com").build();
        userRepository.save(user);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullName() {
        User user = User.builder().email("User@mail.com").build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullEmail() {
        User user = User.builder().name("UserName").build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void shouldGetById() {
        User user = User.builder().name("UserName").email("User@mail.com").build();
        userRepository.save(user);
        User result = userRepository.findById(user.getId()).get();
        Assertions.assertEquals(user.getId(), result.getId());
    }

    @Test
    void shouldGetAllEntity() {
        User user = User.builder().name("UserName").email("User@mail.com").build();
        User user2 = User.builder().name("UserName2").email("User2@mail.com").build();
        userRepository.save(user);
        userRepository.save(user2);
        List<User> result = userRepository.findAll();
        Assertions.assertEquals(2, result.size());
    }
}
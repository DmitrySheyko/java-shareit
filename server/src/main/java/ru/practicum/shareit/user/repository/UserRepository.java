package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Interface of JpaRepository for entity {@link User}
 *
 * @author DmitrySheyko
 */
public interface UserRepository extends JpaRepository<User, Long> {
}

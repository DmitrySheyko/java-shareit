package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class UserStorageImpl implements UserStorage {
    private Map<Long, User> mapOfUsers = new HashMap<>();
    private static Long userId = 0L;

    @Override
    public User add(User user) {
        Long userId = generateUserid();
        user.setId(userId);
        mapOfUsers.put(userId, user);
        return user;
    }

    @Override
    public User update(Long userId, User user) {
        mapOfUsers.put(userId, user);
        return user;
    }

    @Override
    public User getById(Long userId) {
        return mapOfUsers.get(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<User>(mapOfUsers.values());
    }

    private Long generateUserid() {
        return userId++;
    }
}

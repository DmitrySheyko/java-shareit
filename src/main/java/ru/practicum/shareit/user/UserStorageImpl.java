package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class UserStorageImpl implements UserStorage {
    private Map<Long, User> mapOfUsers;
    private static Long userId = 0L;

    @Override
    public User add(User user) {
        System.out.println("Storage");
        Long userId = generateUserid();
        user.setId(userId);
        mapOfUsers.put(userId, user);
        return user;
    }

    @Override
    public User update(Long userId, User user) {
        user.setId(userId);
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

    public String delete (Long userId){
        mapOfUsers.remove(userId);
        return String.format("Пользователь id=%s успешно удален", userId);
    }

    private Long generateUserid() {
        return ++userId;
    }

    public Boolean checkIsUserInStorage(Long userId) {
        return mapOfUsers.containsKey(userId);
    }

    public Boolean checkIsUserInStorage(User user) {
        return mapOfUsers.containsKey(user.getId());
    }

    public Boolean checkIsUserEmailInStorage(User user) {
        return mapOfUsers.values().stream()
                .map(User::getEmail)
                .anyMatch(email-> email.equals(user.getEmail()));
    }
}

package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class userServiceImpl implements UserService {
    @Override
    public UserDto add(User user) {
        return null;
    }

    @Override
    public UserDto update(Long userId, User user) {
        return null;
    }

    @Override
    public UserDto getById(Long userId) {
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return null;
    }
}

package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.Optional;

@Service
@Validated
public class UserClient extends BaseClient {
    private final UserRequestDto userRequestDto;
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
                      UserRequestDto userRequestDto) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.userRequestDto = userRequestDto;
    }

    public ResponseEntity<Object> add(UserRequestDto requestDto) {
        return post("", requestDto);
    }

    public ResponseEntity<Object> update(Long userId, UserRequestDto requestDto) {
        Optional.ofNullable(requestDto.getName()).ifPresent(userRequestDto::checkName);
        Optional.ofNullable(requestDto.getEmail()).ifPresent(userRequestDto::checkEmail);
        return patch("/" + userId, requestDto);
    }

    public ResponseEntity<Object> getById(Long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAll() {
        return get("");
    }

    public ResponseEntity<Object> delete(Long userId) {
        return delete("/" + userId);
    }
}

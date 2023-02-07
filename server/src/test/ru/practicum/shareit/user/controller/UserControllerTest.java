package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(ErrorHandler.class)
                .build();
    }

    @Test
    void add() throws Exception {

        UserDto userDtoForAdd = UserDto.builder().name("UserName").email("User@email.com").build();

        when(userService.add(userDtoForAdd)).thenReturn(userDtoForAdd);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoForAdd))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoForAdd.getId()), Long.class));


    }

    @Test
    void update() throws Exception {
        UserDto userDtoForUpdate = UserDto.builder().id(1L).name("UserName").email("User@email.com").build();
        UserDto requestBody = UserDto.builder().name("UserName").email("User@email.com").build();
        UserDto userDtoAfterUpdate = UserDto.builder().id(1L).name("UserName").email("User@email.com").build();

        when(userService.update(userDtoForUpdate)).thenReturn(userDtoAfterUpdate);

        mvc.perform(patch("/users/{id}", 1)
                        .content(mapper.writeValueAsString(requestBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void getById() throws Exception {
        UserDto userDtoForGetById = UserDto.builder().id(1L).name("UserName").email("User@email.com").build();

        when(userService.getById(1L)).thenReturn(userDtoForGetById);

        mvc.perform(get("/users/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoForGetById.getId()), Long.class));
    }

    @Test
    void getAll() throws Exception {
        List<UserDto> result = List.of(
                UserDto.builder().id(1L).name("UserName").email("User@email.com").build(),
                UserDto.builder().id(2L).name("UserName2").email("User2@email.com").build()
        );

        when(userService.getAll()).thenReturn(result);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void delete() throws Exception {
        Map<String, Long> result = Map.of("Успешно удален пользователь id=", 1L);

        when(userService.delete(1L)).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasKey("Успешно удален пользователь id=")))
                .andExpect(jsonPath("$", Matchers.hasValue(1)));
    }
}
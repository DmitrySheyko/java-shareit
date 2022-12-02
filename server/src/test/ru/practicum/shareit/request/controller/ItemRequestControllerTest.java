package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.OutputItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .setControllerAdvice(ErrorHandler.class)
                .build();
    }

    @Test
    void add() throws Exception {
        InputItemRequestDto requestDtoForAdd = InputItemRequestDto.builder().requestor(1L).description("Description").build();
        InputItemRequestDto requestBody = InputItemRequestDto.builder().description("Description").build();
        OutputItemRequestDto responseDtoForAdd = OutputItemRequestDto.builder().id(1L).description("Description").build();

        when(itemRequestService.add(requestDtoForAdd)).thenReturn(responseDtoForAdd);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestBody))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDtoForAdd.getId()), Long.class));
    }

    @Test
    void getAllOwn() throws Exception {
        OutputItemRequestDto responseDtoForAdd = OutputItemRequestDto.builder().id(1L).description("Description").build();
        OutputItemRequestDto responseDtoForAdd2 = OutputItemRequestDto.builder().id(2L).description("Description2").build();
        List<OutputItemRequestDto> result = List.of(responseDtoForAdd, responseDtoForAdd2);

        when(itemRequestService.getAllOwn(1L)).thenReturn(result);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void getAll() throws Exception {
        OutputItemRequestDto responseDtoForAdd = OutputItemRequestDto.builder().id(1L).description("Description")
                .build();
        OutputItemRequestDto responseDtoForAdd2 = OutputItemRequestDto.builder().id(2L).description("Description2")
                .build();
        List<OutputItemRequestDto> result = List.of(responseDtoForAdd, responseDtoForAdd2);

        when(itemRequestService.getAll(1L, 0, 10)).thenReturn(result);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        when(itemRequestService.getAll(1L, 0, 10)).thenReturn(result);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(-1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void getById() throws Exception {
        OutputItemRequestDto responseDtoForAdd = OutputItemRequestDto.builder().id(1L).description("Description").build();

        when(itemRequestService.getById(1L, 1L)).thenReturn(responseDtoForAdd);

        mvc.perform(get("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDtoForAdd.getId()), Long.class));
    }
}
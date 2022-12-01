package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .setControllerAdvice(IllegalArgumentException.class)
                .build();
    }

    @Test
    void add() throws Exception {
        ItemRequestDto requestDtoForAdd = ItemRequestDto.builder()
                .id(null).owner(1L).name("Test name").description("Test description").available(true).build();
        ItemRequestDto requestBody = ItemRequestDto.builder()
                .name("Test name").description("Test description").available(true).build();
        ItemResponseDto responseDtoForAdd = ItemResponseDto.builder()
                .id(1L).name("Test name").description("Test description").available(true).build();

        when(itemService.add(requestDtoForAdd)).thenReturn(responseDtoForAdd);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(requestBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDtoForAdd.getId()), Long.class));
    }

    @Test
    void update() throws Exception {
        ItemRequestDto requestDtoForUpdate = ItemRequestDto.builder()
                .id(1L).owner(1L).name("Test name").description("Test description").available(true).build();
        ItemRequestDto requestBody = ItemRequestDto.builder()
                .name("Test name").description("Test description").available(true).build();
        ItemResponseDto responseDtoForUpdate = ItemResponseDto.builder()
                .id(1L).name("Test name").description("Test description").available(true).build();

        when(itemService.update(requestDtoForUpdate)).thenReturn(responseDtoForUpdate);

        mvc.perform(patch("/items/{id}", 1)
                        .content(mapper.writeValueAsString(requestBody))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDtoForUpdate.getId()), Long.class));
    }

    @Test
    void getById() throws Exception {
        ItemResponseDto responseDtoForGetById = ItemResponseDto.builder()
                .id(1L).name("Test name").description("Test description").available(true).build();

        when(itemService.getById(1L, 1L)).thenReturn(responseDtoForGetById);

        mvc.perform(get("/items/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDtoForGetById.getId()), Long.class));
    }

    @Test
    void findAllByOwner() throws Exception {
        ItemResponseDtoForOwner response1 = ItemResponseDtoForOwner.builder()
                .id(1L).name("Test name").description("Test description").available(true).build();
        ItemResponseDtoForOwner response2 = ItemResponseDtoForOwner.builder()
                .id(2L).name("Test name2").description("Test description2").available(true).build();
        List<ItemResponseDtoForOwner> result = List.of(response1, response2);

        when(itemService.getAllByOwner(1L, 0, 10)).thenReturn(result);

        mvc.perform(get("/items", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void search() throws Exception {
        ItemResponseDto response1 = ItemResponseDto.builder()
                .id(1L).name("Test name").description("Test description search").available(true).build();
        ItemResponseDto response2 = ItemResponseDto.builder()
                .id(2L).name("Test name2 search").description("Test description2").available(true).build();
        List<ItemResponseDto> result = List.of(response1, response2);

        when(itemService.search("search", 0, 10)).thenReturn(result);

        mvc.perform(get("/items/search", 1)
                        .param("text", "search")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
        mvc.perform(get("/items/search", 1)
                        .param("text", "search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void addComment() throws Exception {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .text("Текст комментария про издедие").author(1L).item(1L).build();
        CommentRequestDto requestBody = CommentRequestDto.builder()
                .text("Текст комментария про издедие").build();
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(1L).text("Текст комментария про издедие").authorName("User name").build();

        when(itemService.addComment(commentRequestDto)).thenReturn(commentResponseDto);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(mapper.writeValueAsString(requestBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentResponseDto.getId()), Long.class));
    }
}
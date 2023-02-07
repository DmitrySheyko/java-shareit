package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ErrorHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

    }

    @Test
    void add() throws Exception {
        BookingRequestDto requestDtoForAdd = BookingRequestDto.builder().bookerId(1L).itemId(1L).build();
        BookingRequestDto requestBody = BookingRequestDto.builder().itemId(1L).build();
        BookingResponseDto responseDtoForAdd = BookingResponseDto.builder().id(1L).build();

        when(bookingService.add(requestDtoForAdd)).thenReturn(responseDtoForAdd);

        mvc.perform(post("/bookings")
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
        BookingResponseDto responseDtoForUpdate = BookingResponseDto.builder().id(1L).build();

        when(bookingService.update(1L, 1L, true)).thenReturn(responseDtoForUpdate);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDtoForUpdate.getId()), Long.class));
    }

    @Test
    void getById() throws Exception {
        BookingResponseDto responseDtoForGetById = BookingResponseDto.builder().id(1L).build();

        when(bookingService.getById(1L, 1L)).thenReturn(responseDtoForGetById);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDtoForGetById.getId()), Long.class));
    }

    @Test
    void getAllBookingsByBookerId() throws Exception {
        BookingResponseDto responseDtoForGetAllByBooker = BookingResponseDto.builder().id(1L).build();
        BookingResponseDto responseDtoForGetAllByBooker2 = BookingResponseDto.builder().id(1L).build();
        List<BookingResponseDto> result = List.of(responseDtoForGetAllByBooker, responseDtoForGetAllByBooker2);

        when(bookingService.getAllBookingsByBookerId(1L, "ALL", 0, 10)).thenReturn(result);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        when(bookingService.getAllBookingsByBookerId(1L, "ALL", 0, 10)).thenReturn(result);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        when(bookingService.getAllBookingsByBookerId(1L, "ALL", 0, 10)).thenReturn(result);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(-1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void getAllBookingsByOwnerItems() throws Exception {
        BookingResponseDto responseDtoForGetAllByOwner = BookingResponseDto.builder().id(1L).build();
        BookingResponseDto responseDtoForGetAllByOwner2 = BookingResponseDto.builder().id(1L).build();
        List<BookingResponseDto> result = List.of(responseDtoForGetAllByOwner, responseDtoForGetAllByOwner2);

        when(bookingService.getAllBookingsByOwnerItems(1L, "ALL", 0, 10)).thenReturn(result);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        when(bookingService.getAllBookingsByOwnerItems(1L, "ALL", 0, 10)).thenReturn(result);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        when(bookingService.getAllBookingsByOwnerItems(1L, "ALL", 0, 10)).thenReturn(result);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(-1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }
}
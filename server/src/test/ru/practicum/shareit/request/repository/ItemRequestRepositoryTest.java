package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.Instant;
import java.util.List;

@DataJpaTest(properties = "spring.sql.init.data-locations=data-test.sql")
class ItemRequestRepositoryTest {

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Test
    void shouldAddIdWhenSaveNewEntity() {
        ItemRequest itemRequest = ItemRequest.builder().requestor(1L).description("ItemRequest description")
                .created(Instant.now()).build();
        Assertions.assertNull(itemRequest.getId());
        itemRequestRepository.save(itemRequest);
        Assertions.assertNotNull(itemRequest.getId());
    }

    @Test
    void shouldThrowExceptionWhenSaveNullRequestor() {
        ItemRequest itemRequest = ItemRequest.builder().description("ItemRequest description")
                .created(Instant.now()).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullDescription() {
        ItemRequest itemRequest = ItemRequest.builder().requestor(1L).created(Instant.now()).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullCreated() {
        ItemRequest itemRequest = ItemRequest.builder().requestor(1L).description("ItemRequest description").build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @Test
    void findAllByRequestorOrderByCreatedDesc() {
        Long requestorId = 1L;
        int numberOfRequests = 3;
        List<ItemRequest> result = itemRequestRepository.findAllByRequestorOrderByCreatedDesc(requestorId);
        Assertions.assertEquals(numberOfRequests, result.size());
        Assertions.assertEquals(3, result.get(0).getId());
        Assertions.assertEquals(2, result.get(1).getId());
        Assertions.assertEquals(1, result.get(2).getId());
    }

    @Test
    void findAllByRequestorNot() {
        Long requestorId = 1L;
        int numberOfRequests = 2;
        Page<ItemRequest> oneRequestPage = itemRequestRepository.findAllByRequestorNot(
                PageRequest.of(0, 1, Sort.by("created").descending()), requestorId);
        Page<ItemRequest> tenRequestPage = itemRequestRepository.findAllByRequestorNot(
                PageRequest.of(0, 10, Sort.by("created").descending()), requestorId);
        Assertions.assertEquals(numberOfRequests, tenRequestPage.getTotalElements());
        Assertions.assertEquals(1, oneRequestPage.toList().size());
        Assertions.assertEquals(5, tenRequestPage.toList().get(0).getId());
        Assertions.assertEquals(4, tenRequestPage.toList().get(1).getId());
    }
}
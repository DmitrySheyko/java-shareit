package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Interface of JpaRepository for entity {@link ItemRequest}.
 *
 * @author DmitrySheyko
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorOrderByCreatedDesc(Long userId);

    Page<ItemRequest> findAllByRequestorNot(Pageable pageable, Long userId);

}

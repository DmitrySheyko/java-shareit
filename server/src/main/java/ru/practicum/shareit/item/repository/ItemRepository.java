package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

/**
 * Interface of JpaRepository for entity {@link Item}.
 *
 * @author DmitrySheyko
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByOwner(Long userId, Pageable pageable);

    @Query(" select i from Item i " +
            "where i.available = true and " +
            "(upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    Page<Item> search(String text, Pageable pageable);

}

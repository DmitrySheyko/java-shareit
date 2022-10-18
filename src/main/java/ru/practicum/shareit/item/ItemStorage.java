package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;

@Repository
@AllArgsConstructor
public class ItemStorage {
    Map<Long, Item> mapOfItems;
    private static long itemId = 0L;

    public Item add(Item item) {
        Long itemId = generateItemId();
        item.setId(itemId);
        mapOfItems.put(itemId, item);
        return item;
    }

    private Long generateItemId() {
        return ++itemId;
    }
}

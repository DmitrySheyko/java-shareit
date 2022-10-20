package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public Item update (Long itemId, Item itemForUpdate){
        mapOfItems.put(itemId, itemForUpdate);
        return itemForUpdate;
    }

    public Item getById (Long itemId){
        return mapOfItems.get(itemId);
    }

    public List<Item> getAll (Long userId){
        return mapOfItems.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .collect(Collectors.toList());
    }

    public List<Item> search (String text){
        return mapOfItems.values().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private Long generateItemId() {
        return ++itemId;
    }

    public boolean checkIsObjectInStorage(Item item){
        return mapOfItems.containsValue(item);
    }

    public boolean checkIsObjectInStorage(Long itemId){
        return mapOfItems.containsKey(itemId);
    }
}

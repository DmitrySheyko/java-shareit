package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.interfaces.Storages;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ItemStorage implements Storages<Item> {
    Map<Long, Item> mapOfItems;
    private static long itemId = 0L;

    @Override
    public Item add(Item item) {
        Long itemId = generateItemId();
        item.setId(itemId);
        mapOfItems.put(itemId, item);
        return item;
    }

    @Override
    public Item update(Item itemForUpdate) {
        mapOfItems.put(itemForUpdate.getId(), itemForUpdate);
        return itemForUpdate;
    }

    @Override
    public Item getById(Long itemId) {
        return mapOfItems.get(itemId);
    }


    public List<Item> getAll(Long userId) {
        return mapOfItems.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(mapOfItems.values());
    }

    public List<Item> search(String text) {
        return mapOfItems.values().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String delete(Long userId) {
        mapOfItems.remove(userId);
        return String.format("Пользователь id=%s успешно удален.", userId);
    }

    @Override
    public Boolean checkIsObjectInStorage(Item item) {
        return mapOfItems.containsValue(item);
    }

    @Override
    public Boolean checkIsObjectInStorage(Long itemId) {
        return mapOfItems.containsKey(itemId);
    }

    private Long generateItemId() {
        return ++itemId;
    }
}

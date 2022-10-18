package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;

    public Item add(Item item){
        Item newItem = itemStorage.add(item);
        log.info(String.format("Объект id=%s успешно добавлен", item.getId()));
        return newItem;
    }
}

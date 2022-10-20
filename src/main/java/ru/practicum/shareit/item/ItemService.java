package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    public ItemDto add(ItemDto itemDto, Long userId) {
        if (!userStorage.checkIsUserInStorage(userId)) {
            log.warn(String.format("Пользователь userId=%s не найден", userId));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", userId));
        }
        try {
            itemDto.setOwner(userId);
            Item addedItem = itemStorage.add(itemMapper.toItem(itemDto));
            ItemDto addedItemDto = ItemMapper.toItemDto(addedItem);
            log.info(String.format("Объект id=%s успешно добавлен", addedItemDto.getId()));
            System.out.println(addedItem);
            System.out.println(addedItemDto);
            return addedItemDto;
        } catch (NullPointerException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public ItemDto update(Long itemId, ItemDto itemDtoForUpdate, Long userId) {
        if (!userStorage.checkIsUserInStorage(userId)) {
            log.warn(String.format("Пользователь userId=%s не найден", userId));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", userId));
        }
        if (!itemStorage.checkIsObjectInStorage(itemId)) {
            log.warn(String.format("Объект itemId=%s не найден", itemId));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemId));
        }
        if (itemStorage.getById(itemId).getOwner() != userId) {
            log.warn(String.format("У пользователя userId=%s нет объекта itemId=%s", userId, itemId));
            throw new ObjectNotFoundException(String.format("У пользователя userId=%s нет объекта itemId=%s",
                    userId, itemId));
        }
        Item itemFromStorage = itemStorage.getById(itemId);
        if (itemDtoForUpdate.getId() == null) {
            itemDtoForUpdate.setId(itemId);
        }
        if (itemDtoForUpdate.getName() == null) {
            itemDtoForUpdate.setName(itemFromStorage.getName());
        }
        if (itemDtoForUpdate.getDescription() == null) {
            itemDtoForUpdate.setDescription(itemFromStorage.getDescription());
        }
        if (itemDtoForUpdate.getAvailable() == null) {
            itemDtoForUpdate.setAvailable(itemFromStorage.getAvailable());
        }
        itemDtoForUpdate.setOwner(userId);
        itemStorage.update(itemId, itemMapper.toItem(itemDtoForUpdate));
        return itemDtoForUpdate;
    }

    public ItemDto getById(Long itemId) {
        if (!itemStorage.checkIsObjectInStorage(itemId)) {
            log.warn(String.format("Объект itemId=%s не найден", itemId));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemId));
        }
        Item item = itemStorage.getById(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        log.info(String.format("Объект itemId=%s успешно получен.", itemId));
        return itemDto;
    }

    public List<ItemDto> getAll(Long userId) {
        List<Item> listOfItems = itemStorage.getAll(userId);
        List<ItemDto> listOfItemDto = listOfItems.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }

    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> listOfItems = itemStorage.search(text);
        List<ItemDto> listOfItemDto = listOfItems.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }
}

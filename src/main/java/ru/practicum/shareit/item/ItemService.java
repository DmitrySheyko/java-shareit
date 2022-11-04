package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.interfaces.Dto;
import ru.practicum.shareit.interfaces.Mappers;
import ru.practicum.shareit.interfaces.Services;
import ru.practicum.shareit.item.dto.ItemDtoForOtherUsers;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    public ItemDtoForOtherUsers add(ItemDtoForOtherUsers itemDtoForOtherUsers) {
        if (!userService.checkIsObjectInStorage(itemDtoForOtherUsers.getOwner())) {
            log.warn(String.format("Пользователь userId=%s не найден", itemDtoForOtherUsers.getOwner()));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", itemDtoForOtherUsers.getOwner()));
        }
        Item newItem = itemMapper.DtoForOtherUsersToEntity(itemDtoForOtherUsers);
        Item addedItem = itemRepository.save(newItem);
        ItemDtoForOtherUsers addedItemDtoForOtherUsers = itemMapper.toDtoForOtherUsers(addedItem);
        log.info(String.format("Объект id=%s успешно добавлен", addedItemDtoForOtherUsers.getId()));
        return addedItemDtoForOtherUsers;
    }

    public ItemDtoForOtherUsers update(ItemDtoForOtherUsers itemDtoForOtherUsersForUpdate) {
        if (!userService.checkIsObjectInStorage(itemDtoForOtherUsersForUpdate.getOwner())) {
            log.warn(String.format("Пользователь userId=%s не найден", itemDtoForOtherUsersForUpdate.getOwner()));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден",
                    itemDtoForOtherUsersForUpdate.getOwner()));
        }
        if (!checkIsObjectInStorage(itemDtoForOtherUsersForUpdate.getId())) {
            log.warn(String.format("Объект itemId=%s не найден", itemDtoForOtherUsersForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemDtoForOtherUsersForUpdate.getId()));
        }
        Item itemFromStorage;
        Optional<Item> optionalItemFromStorage = itemRepository.findById(itemDtoForOtherUsersForUpdate.getId());
        if (optionalItemFromStorage.isEmpty()) {
            log.warn(String.format("Информация об объекту itemId=%s не найдена", itemDtoForOtherUsersForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Информация об объекту itemId=%s не найдена",
                    itemDtoForOtherUsersForUpdate.getId()));
        } else {
            itemFromStorage = optionalItemFromStorage.get();
        }
        if (!Objects.equals(itemFromStorage.getOwner(), itemDtoForOtherUsersForUpdate.getOwner())) {
            log.warn(String.format("У пользователя userId=%s нет объекта itemId=%s",
                    itemDtoForOtherUsersForUpdate.getOwner(), itemDtoForOtherUsersForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("У пользователя userId=%s нет объекта itemId=%s",
                    itemDtoForOtherUsersForUpdate.getOwner(), itemDtoForOtherUsersForUpdate.getId()));
        }
        itemDtoForOtherUsersForUpdate.setId(Optional.ofNullable(itemDtoForOtherUsersForUpdate.getId()).orElse(itemFromStorage.getId()));
        itemDtoForOtherUsersForUpdate.setName(Optional.ofNullable(itemDtoForOtherUsersForUpdate.getName()).orElse(itemFromStorage.getName()));
        itemDtoForOtherUsersForUpdate.setDescription(Optional.ofNullable(itemDtoForOtherUsersForUpdate.getDescription())
                .orElse(itemFromStorage.getDescription()));
        itemDtoForOtherUsersForUpdate.setAvailable(Optional.ofNullable(itemDtoForOtherUsersForUpdate.getAvailable())
                .orElse(itemFromStorage.getAvailable()));
        itemRepository.save(itemMapper.DtoForOtherUsersToEntity(itemDtoForOtherUsersForUpdate));
        return itemDtoForOtherUsersForUpdate;
    }


    public Dto getById(Long userId, Long itemId) {
        if (!userService.checkIsObjectInStorage(userId)) {
            log.warn(String.format("Пользователь userId=%s не найден", userId));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", userId));
        }
        if (!checkIsObjectInStorage(itemId)) {
            log.warn(String.format("Объект itemId=%s не найден", itemId));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemId));
        }
        Item item = findById(itemId);
        if (userId.equals(item.getOwner())) {
            ItemDtoForOwner itemDtoForOwner = itemMapper.toDtoForOwner(item);
            log.info(String.format("Объект itemId=%s успешно получен.", itemId));
            return itemDtoForOwner;
        }
        ItemDtoForOtherUsers itemDtoForOtherUsers = itemMapper.toDtoForOtherUsers(item);
        log.info(String.format("Объект itemId=%s успешно получен.", itemId));
        return itemDtoForOtherUsers;
    }

    public Item findById(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            log.warn(String.format("Информция об объекте itemId=%s не найдена", itemId));
            throw new ObjectNotFoundException(String.format("Информция об объекте itemId=%s не найдена", itemId));
        } else {
            return optionalItem.get();
        }
    }




    public List<ItemDtoForOwner> getAllByOwner(Long userId) {
        if (!userService.checkIsObjectInStorage(userId)) {
            log.warn(String.format("Пользователь userId=%s не найден", userId));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", userId));
        }
        List<Item> listOfItems = itemRepository.findAllByOwner(userId);
        if(listOfItems != null) {
            List<ItemDtoForOwner> listOfItemDtoForOwners = listOfItems.stream().map(itemMapper::toDtoForOwner).collect(Collectors.toList());
            log.info("Список объектов успешно получен.");
            return listOfItemDtoForOwners;
        } else {
            return Collections.emptyList();
        }
    }

//    public List<ItemDtoForOtherUsers> getAll() {
//        List<Item> listOfItems = itemRepository.findAll();
//        List<ItemDtoForOtherUsers> listOfItemDtoForOtherUsers = listOfItems.stream().map(itemMapper::toDtoForOtherUsers).collect(Collectors.toList());
//        log.info("Список объектов успешно получен.");
//        return listOfItemDtoForOtherUsers;
//    }


    public List<ItemDtoForOtherUsers> search(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        List<Item> listOfItems = itemRepository.search(text);
        List<ItemDtoForOtherUsers> listOfItemDtoForOtherUsers = listOfItems.stream().map(itemMapper::toDtoForOtherUsers).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDtoForOtherUsers;
    }

    public String delete(Long userId) {
        itemRepository.deleteById(userId);
        log.info(String.format("Пользователь id=%s успешно удален.", userId));
        return String.format("Пользователь id=%s успешно удален.", userId);
    }

    public boolean checkIsObjectInStorage(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    public boolean checkIsObjectInStorage(Item item) {
        return itemRepository.existsById(item.getId());
    }
}

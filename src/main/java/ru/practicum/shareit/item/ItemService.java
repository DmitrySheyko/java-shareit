package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.interfaces.Mappers;
import ru.practicum.shareit.interfaces.Services;
import ru.practicum.shareit.item.dto.ItemDto;
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
public class ItemService implements Services<ItemDto> {
    private final ItemRepository itemRepository;
    private final Mappers<ItemDto, Item> itemMapper;
    private final UserService userService;

    @Override
    public ItemDto add(ItemDto itemDto) {
        if (!userService.checkIsObjectInStorage(itemDto.getOwner())) {
            log.warn(String.format("Пользователь userId=%s не найден", itemDto.getOwner()));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", itemDto.getOwner()));
        }
        Item newItem = itemMapper.toEntity(itemDto);
        Item addedItem = itemRepository.save(newItem);
        ItemDto addedItemDto = itemMapper.toDto(addedItem);
        log.info(String.format("Объект id=%s успешно добавлен", addedItemDto.getId()));
        return addedItemDto;
    }

    @Override
    public ItemDto update(ItemDto itemDtoForUpdate) {
        if (!userService.checkIsObjectInStorage(itemDtoForUpdate.getOwner())) {
            log.warn(String.format("Пользователь userId=%s не найден", itemDtoForUpdate.getOwner()));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден",
                    itemDtoForUpdate.getOwner()));
        }
        if (!checkIsObjectInStorage(itemDtoForUpdate.getId())) {
            log.warn(String.format("Объект itemId=%s не найден", itemDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemDtoForUpdate.getId()));
        }
        Item itemFromStorage;
        Optional<Item> optionalItemFromStorage = itemRepository.findById(itemDtoForUpdate.getId());
        if (optionalItemFromStorage.isEmpty()) {
            log.warn(String.format("Информация об объекту itemId=%s не найдена", itemDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Информация об объекту itemId=%s не найдена",
                    itemDtoForUpdate.getId()));
        } else {
            itemFromStorage = optionalItemFromStorage.get();
        }
        if (!Objects.equals(itemFromStorage.getOwner(), itemDtoForUpdate.getOwner())) {
            log.warn(String.format("У пользователя userId=%s нет объекта itemId=%s",
                    itemDtoForUpdate.getOwner(), itemDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("У пользователя userId=%s нет объекта itemId=%s",
                    itemDtoForUpdate.getOwner(), itemDtoForUpdate.getId()));
        }
        itemDtoForUpdate.setId(Optional.ofNullable(itemDtoForUpdate.getId()).orElse(itemFromStorage.getId()));
        itemDtoForUpdate.setName(Optional.ofNullable(itemDtoForUpdate.getName()).orElse(itemFromStorage.getName()));
        itemDtoForUpdate.setDescription(Optional.ofNullable(itemDtoForUpdate.getDescription())
                .orElse(itemFromStorage.getDescription()));
        itemDtoForUpdate.setAvailable(Optional.ofNullable(itemDtoForUpdate.getAvailable())
                .orElse(itemFromStorage.getAvailable()));
        itemRepository.save(itemMapper.toEntity(itemDtoForUpdate));
        return itemDtoForUpdate;
    }

    @Override
    public ItemDto getById(Long itemId) {
        if (!checkIsObjectInStorage(itemId)) {
            log.warn(String.format("Объект itemId=%s не найден", itemId));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemId));
        }
        Item item;
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            log.warn(String.format("Информция об объекте itemId=%s не найдена", itemId));
            throw new ObjectNotFoundException(String.format("Информция об объекте itemId=%s не найдена", itemId));
        } else {
            item = optionalItem.get();
        }
        ItemDto itemDto = itemMapper.toDto(item);
        log.info(String.format("Объект itemId=%s успешно получен.", itemId));
        return itemDto;
    }

    @Override
    public List<ItemDto> getAll() {
        List<Item> listOfItems = itemRepository.findAll();
        List<ItemDto> listOfItemDto = listOfItems.stream().map(itemMapper::toDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }

    public List<ItemDto> findAllByOwner(Long userId) {
        List<Item> listOfItems = itemRepository.findAllByOwner(userId);
        List<ItemDto> listOfItemDto = listOfItems.stream().map(itemMapper::toDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }

    public List<ItemDto> search(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        List<Item> listOfItems = itemRepository.search(text);
        List<ItemDto> listOfItemDto = listOfItems.stream().map(itemMapper::toDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }

    @Override
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

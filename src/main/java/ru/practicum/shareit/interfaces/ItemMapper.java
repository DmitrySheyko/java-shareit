package ru.practicum.shareit.interfaces;

//import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDtoForOtherUsers;
import ru.practicum.shareit.item.model.Item;

//@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemDtoForOtherUsers itemDtoForOtherUsers);

    ItemDtoForOtherUsers toItemDto(Item item);
}

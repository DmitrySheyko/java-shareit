package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.interfaces.Dto;
import ru.practicum.shareit.item.dto.ItemDtoForOtherUsers;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDtoForOtherUsers add(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDtoForOtherUsers itemDtoForOtherUsers) {
        itemDtoForOtherUsers.setOwner(userId);
        return itemService.add(itemDtoForOtherUsers);
    }

    @PatchMapping("/{id}")
    public ItemDtoForOtherUsers update(@PathVariable(value = "id") Long itemId, @RequestBody ItemDtoForOtherUsers itemDtoForOtherUsersForUpdate,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemDtoForOtherUsersForUpdate.setId(itemId);
        itemDtoForOtherUsersForUpdate.setOwner(userId);
        return itemService.update(itemDtoForOtherUsersForUpdate);
    }

    @GetMapping("/{id}")
    public Dto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable("id") Long itemId) {
        Dto result = itemService.getById(userId, itemId);
        System.out.println(result);
        return result;
    }

    @GetMapping
    public List<ItemDtoForOwner> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoForOtherUsers> search(@PathParam("text") String text) {
        return itemService.search(text);
    }
}
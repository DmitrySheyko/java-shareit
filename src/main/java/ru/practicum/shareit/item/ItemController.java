package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.servlet.ServletOutputStream;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Collections;
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
    public ItemDto add (@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto){
        System.out.println("userID = " + userId);
        return itemService.add(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update (@PathVariable(value = "id") Long itemId, @RequestBody  ItemDto itemDtoForUpdate,
                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.update(itemId, itemDtoForUpdate, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable("id") Long itemId){
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAll (@RequestHeader("X-Sharer-User-Id") Long userId){
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List <ItemDto> search (@PathParam("text") String text){
        return itemService.search(text);
    }
}

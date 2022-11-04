package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.interfaces.Mappers;
import ru.practicum.shareit.item.dto.ItemDtoForOtherUsers;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@Component
@Validated
@AllArgsConstructor
public class ItemMapper {
//    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public ItemDtoForOtherUsers toDtoForOtherUsers(Item item) {
        return ItemDtoForOtherUsers.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .lastBooking(null)
                .nextBooking(null)
                .build();
    }

    public ItemDtoForOwner toDtoForOwner(Item item) {
        if (item == null) {
            return null;
        } else {
            return ItemDtoForOwner.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner())
                    .request(item.getRequest())
                    .lastBooking(findLastBookingsByItemId(item.getId()))
                    .nextBooking(findNextBookingsByItemId(item.getId()))
                    .build();
        }
    }

    @Valid
    public Item DtoForOtherUsersToEntity(ItemDtoForOtherUsers itemDtoForOtherUsers) {
        return Item.builder()
                .id(itemDtoForOtherUsers.getId())
                .name(itemDtoForOtherUsers.getName())
                .description(itemDtoForOtherUsers.getDescription())
                .available(itemDtoForOtherUsers.getAvailable())
                .owner(itemDtoForOtherUsers.getOwner())
                .request(itemDtoForOtherUsers.getRequest())
                .build();
    }

    public Booking findLastBookingsByItemId(Long itemId){
        List<Booking> listOfBookings = bookingRepository.findLastBookingsByItemId(itemId, Instant.now());
        if(listOfBookings.isEmpty()){
            return null;
        } else {
            return listOfBookings.get(0);
        }
    }

    public Booking findNextBookingsByItemId(Long itemId){
        List<Booking> listOfBookings = bookingRepository.findNextBookingsByItemId(itemId, Instant.now());
        if(listOfBookings.isEmpty()){
            return null;
        } else {
            return listOfBookings.get(0);
        }
    }
}

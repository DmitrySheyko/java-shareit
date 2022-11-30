package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Validated
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userServiceImpl;

    @Override
    public ItemResponseDto add(ItemRequestDto itemRequestDto) {
        userServiceImpl.checkIsObjectInStorage(itemRequestDto.getOwner());
        Item newItem = itemMapper.requestDtoToEntity(itemRequestDto);
        Item addedItem = itemRepository.save(newItem);
        ItemResponseDto addedItemResponseDto = itemMapper.toItemResponseDto(addedItem,
                Collections.emptyList());
        log.info(String.format("Объект id=%s успешно добавлен", addedItemResponseDto.getId()));
        return addedItemResponseDto;
    }

    @Override
    public ItemResponseDto update(ItemRequestDto itemRequestDto) {
        userServiceImpl.checkIsObjectInStorage(itemRequestDto.getOwner());
        checkIsItemInStorage(itemRequestDto.getId());
        Item itemFromStorage = findById(itemRequestDto.getId());
        if (!Objects.equals(itemFromStorage.getOwner(), itemRequestDto.getOwner())) {
            log.warn(String.format("У пользователя userId=%s нет прав редактировать объект itemId=%s",
                    itemRequestDto.getOwner(), itemRequestDto.getId()));
            throw new ObjectNotFoundException(String.format("У пользователя userId=%s нет прав редактировать объект " +
                    "itemId=%s", itemRequestDto.getOwner(), itemRequestDto.getId()));
        }
        itemRequestDto.setId(Optional.ofNullable(itemRequestDto.getId()).orElse(itemFromStorage.getId()));
        itemRequestDto.setName(Optional.ofNullable(itemRequestDto.getName()).orElse(itemFromStorage.getName()));
        itemRequestDto.setDescription(Optional.ofNullable(itemRequestDto.getDescription())
                .orElse(itemFromStorage.getDescription()));
        itemRequestDto.setAvailable(Optional.ofNullable(itemRequestDto.getAvailable())
                .orElse(itemFromStorage.getAvailable()));
        Item updatedItem = itemRepository.save(itemMapper.requestDtoToEntity(itemRequestDto));
        List<CommentResponseDto> listOfComments = findListOfComments(itemRequestDto.getId());
        return itemMapper.toItemResponseDto(updatedItem, listOfComments);
    }

    @Override
    public ResponseDto getById(Long userId, Long itemId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        checkIsItemInStorage(itemId);
        Item item = findById(itemId);
        List<CommentResponseDto> listOfComments = findListOfComments(itemId);
        if (userId.equals(item.getOwner())) {
            ItemResponseDtoForOwner itemResponseDtoForOwner = itemMapper.toItemResponseDtoForOwner(item, listOfComments);
            log.info(String.format("Объект itemId=%s успешно получен.", itemId));
            return itemResponseDtoForOwner;
        }
        ItemResponseDto itemResponseDto = itemMapper.toItemResponseDto(item, listOfComments);
        log.info(String.format("Объект itemId=%s успешно получен.", itemId));
        return itemResponseDto;
    }

    @Override
    public List<ItemResponseDtoForOwner> getAllByOwner(Long userId, int from, int size) {
        userServiceImpl.checkIsObjectInStorage(userId);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Item> listOfItems = itemRepository.findAllByOwner(userId, pageable);
        if (listOfItems != null) {
            List<ItemResponseDtoForOwner> listOfItemResponseDtoForOwners = listOfItems.stream()
                    .map(item -> itemMapper.toItemResponseDtoForOwner(item, findListOfComments(item.getId())))
                    .collect(Collectors.toList());
            log.info("Список объектов успешно получен.");
            return listOfItemResponseDtoForOwners;
        }
        log.info("Список объектов пуст.");
        return Collections.emptyList();
    }

    @Override
    public CommentResponseDto addComment(CommentRequestDto commentRequestDto) {
        userServiceImpl.checkIsObjectInStorage(commentRequestDto.getAuthor());
        checkIsItemInStorage(commentRequestDto.getItem());
        if (checkIsUserCanMakeComment(commentRequestDto.getAuthor(), commentRequestDto.getItem())) {
            Comment comment = commentMapper.toEntity(commentRequestDto);
            comment.setCreated(Instant.now());
            Comment savedComment = commentRepository.save(comment);
            CommentResponseDto result = commentMapper.toCommentResponseDto(savedComment);
            log.info(String.format("Комментарий Id=%s на объект ItemId=%s успешно добавлен  пользователем UserId=%s",
                    savedComment.getId(), savedComment.getItem(), commentRequestDto.getAuthor()));
            return result;
        } else {
            log.warn(String.format("Пользователь userId=%s не может оставить комментарий", commentRequestDto.getAuthor()));
            throw new ValidationException(String.format("Пользователь userId=%s не может оставить комментарий", commentRequestDto.getAuthor()));
        }
    }

    @Override
    public List<ItemResponseDto> search(String text, int from, int size) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Item> listOfItems = itemRepository.search(text, pageable);
        List<ItemResponseDto> listOfItemResponseDtoForOtherUsers = listOfItems.stream()
                .map(item -> itemMapper.toItemResponseDto(item, findListOfComments(item.getId())))
                .collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemResponseDtoForOtherUsers;
    }

    @Override
    public String delete(Long userId) {
        itemRepository.deleteById(userId);
        log.info(String.format("Пользователь id=%s успешно удален.", userId));
        return String.format("Пользователь id=%s успешно удален.", userId);
    }

    @Override
    public void checkIsItemInStorage(Long itemId) {
        if (!(itemId != null && itemRepository.existsById(itemId))) {
            log.warn(String.format("Объект itemId=%s не найден", itemId));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemId));
        }
    }

    @Override
    public void checkIsItemAvailable(Long itemId) {
        if (!findById(itemId).getAvailable()) {
            log.warn(String.format("Объект itemId=%s не доступен", itemId));
            throw new ValidationException(String.format("Объект itemId=%s не доступен", itemId));
        }
    }

    private List<CommentResponseDto> findListOfComments(Long itemId) {
        return commentRepository.findByItem(itemId).stream()
                .map(commentMapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    private Boolean checkIsUserCanMakeComment(Long userId, Long itemId) {
        if (findById(itemId).getOwner().equals(itemId)) {
            return false;
        }
        List<Booking> result = bookingRepository.findAllByBookerIdAndItemIdAndEndIsBeforeAndStatus(userId, itemId,
                Instant.now(), BookingStatus.APPROVED);
        return !result.isEmpty();
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
}

package ru.practicum.shareit.interfaces;

public interface Mappers<T, E> {

    T toDtoForOtherUsers(E obj);

    E DtoForOtherUsersToEntity(T objDto);
}

package ru.practicum.shareit.interfaces;

public interface Mappers<T, E> {

    T toDto(E obj);

    E toEntity(T objDto);
}

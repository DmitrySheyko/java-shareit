package ru.practicum.shareit.interfaces;

import java.util.List;

public interface Storages<T> {
    T add(T obj);

    T update(T obj);

    T getById(Long id);

    List<T> getAll();

    Boolean checkIsObjectInStorage(Long id);

    Boolean checkIsObjectInStorage(T obj);

    String delete(Long id);
}

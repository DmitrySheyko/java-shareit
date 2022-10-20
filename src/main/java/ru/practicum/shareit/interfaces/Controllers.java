package ru.practicum.shareit.interfaces;

import java.util.List;

public interface Controllers<T> {

    T add(T obj);

    T update(Long id, T obj);

    T getById(Long id);

    List<T> getAll();

    String delete(Long id);

}

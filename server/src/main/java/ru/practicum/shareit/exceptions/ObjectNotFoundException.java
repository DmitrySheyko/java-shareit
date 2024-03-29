package ru.practicum.shareit.exceptions;

/**
 * Class of exception {@link ObjectNotFoundException}.
 *
 * @author DmitrySheyko
 */
public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }

}
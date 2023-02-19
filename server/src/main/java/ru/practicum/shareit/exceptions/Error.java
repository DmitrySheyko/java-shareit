package ru.practicum.shareit.exceptions;

/**
 * Class of statuses of entity {@link Error}.
 * Used for returning information about exceptions
 *
 * @author DmitrySheyko
 */
public class Error {

    private final String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}

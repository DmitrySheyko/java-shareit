package ru.practicum.shareit.exceptions;

public class Error {
    private final String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

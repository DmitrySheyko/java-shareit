package ru.practicum.shareit.exceptions;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import java.util.Collections;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ErrorHandlerTest {
    final ErrorHandler errorHandler;

    @Test
    void handlerOfValidationException() {
        Error error = errorHandler.handlerOfValidationException(new ValidationException("message"));
        Assertions.assertEquals(error.getError(), "message");
    }

    @Test
    void handlerOfConstraintValidationException() {
        Error error = errorHandler.handlerOfConstraintValidationException(new ConstraintViolationException("message",
                Collections.emptySet()));
        Assertions.assertEquals(error.getError(), "message");
    }

    @Test
    void handlerOfObjectNotFoundException() {
        Error error = errorHandler.handlerOfObjectNotFoundException(new ObjectNotFoundException("message"));
        Assertions.assertEquals(error.getError(), "message");
    }

    @Test
    void handlerExceptions() {
        Error error = errorHandler.handlerExceptions(new Exception("message"));
        Assertions.assertEquals(error.getError(), "message");
    }
}
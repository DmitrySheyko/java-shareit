package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String handlerOfValidationException(final ValidationException e) {
        return String.format("Ошибка. %s", e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onConstraintValidationException(final ConstraintViolationException e) {
        return String.format("Ошибка. %s", e.getMessage());
//        final List<Violation> violations = e.getConstraintViolations().stream()
//                .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
//                .collect(Collectors.toList());
//        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return String.format("Ошибка. %s", e.getMessage());
//        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
//                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
//                .collect(Collectors.toList());
//        return new ValidationErrorResponse(violations);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String handlerOfObjectNotFoundException(final ObjectNotFoundException e) {
        return String.format("Ошибка. %s", e.getMessage());
    }

    @ExceptionHandler(ConflictErrorException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlerOfConflictErrorException(final ConflictErrorException e) {
        return String.format("Ошибка. %s", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlerExceptions(final Exception e) {
        return String.format("Ошибка. %s", e.getMessage());
    }
}

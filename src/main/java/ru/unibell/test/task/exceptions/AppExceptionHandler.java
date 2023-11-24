package ru.unibell.test.task.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        log.error("Unknown error occurred", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidInputException.class)
    public static ResponseEntity<Object> handleInvalidInputException(InvalidInputException invalidInputException) {
        List<String> details = new ArrayList<>();
        details.add(invalidInputException.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Invalid user input", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public static ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        List<String> details = new ArrayList<>();
        details.add(userNotFoundException.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("User Not Found", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}

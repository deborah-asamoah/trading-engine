package io.turntabl.project.orderprocessingservice.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorBody handlemethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return ErrorBody
                .builder()
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorBody handleConstraintViolation(ConstraintViolationException exception) {
        return ErrorBody
                .builder()
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorBody handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return ErrorBody
                .builder()
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorBody handleIllegalArgumentException(IllegalArgumentException exception) {
        return ErrorBody
                .builder()
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorBody handleNoSuchElementException(NoSuchElementException exception) {
        return ErrorBody
                .builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> exception(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorBody(String.format("Unhandled Exception: %s", exception.getMessage())));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ErrorBody {
        private String message;
    }
}

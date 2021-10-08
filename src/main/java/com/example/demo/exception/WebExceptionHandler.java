package com.example.demo.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s [%s] %s",
                        fieldError.getField(),
                        Objects.toString(fieldError.getRejectedValue(), ""),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        LOGGER.error("Invalid argument error {}: {}", e.getBindingResult().getObjectName(), String.join("\n", messages));

        return response(messages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public final ResponseEntity<Object> handleTransactionNotFoundException(TransactionNotFoundException e) {
        LOGGER.error(e.getMessage());

        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception e) {
        LOGGER.error("Internal server error", e);

        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        List<String> messages = List.of(String.format("%s %s", e.getConstraintName(), e.getLocalizedMessage()));

        LOGGER.error("Constraint violation error: {}", messages);

        return response(messages, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> response(Exception e, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), httpStatus);
    }

    private ResponseEntity<Object> response(List<String> messages, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(messages), httpStatus);
    }

    public static class ErrorResponse {
        private final List<String> messages;

        public ErrorResponse(List<String> messages) {
            this.messages = messages;
        }

        public ErrorResponse(String message) {
            this.messages = Collections.singletonList(message);
        }

        public List<String> getMessages() {
            return messages;
        }
    }
}

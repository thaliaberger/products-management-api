package com.example.products.handler;


import com.example.products.exception.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiError> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            WebRequest request) {

        String error = "missing_parameter";
        String message = String.format("Required parameter '%s' is not present", e.getParameterName());

        ApiError apiError = new ApiError(
                error,
                message,
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false),
                null);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException e,
            WebRequest request) {

        List<ApiError.ValidationError> errors = e.getConstraintViolations().stream()
                .map(violation -> {
                    String field = violation.getPropertyPath().toString();
                    return new ApiError.ValidationError(
                            field.substring(field.lastIndexOf('.') + 1),
                            violation.getMessage()
                    );
                })
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                "validation_error",
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false),
                errors
        );
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            WebRequest request) {

        List<ApiError.ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiError.ValidationError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                "invalid_argument",
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false),
                errors
        );
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException e,
            WebRequest request) {

        ApiError apiError = new ApiError(
                "invalid_argument",
                "Invalid argument type",
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false),
                null
        );
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
package com.example.products.handler;
import com.example.products.dto.ErrorDTO;
import com.example.products.exception.FieldException;
import com.example.products.exception.ProductAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(FieldException.class)
    public ErrorDTO handler(FieldException ex) {
        return new ErrorDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ErrorDTO handler(ProductAlreadyExistsException ex) {
        return new ErrorDTO(
                ex.getMessage(),
                HttpStatus.CONFLICT.value()
        );
    }
}
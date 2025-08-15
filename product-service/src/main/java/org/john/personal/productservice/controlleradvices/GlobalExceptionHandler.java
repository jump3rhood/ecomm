package org.john.personal.productservice.controlleradvices;

import org.john.personal.productservice.dto.ExceptionDTO;
import org.john.personal.productservice.exceptions.CategoryAlreadyExists;
import org.john.personal.productservice.exceptions.CategoryNotFoundException;
import org.john.personal.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleProductNotFoundException(ProductNotFoundException ex){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMessage(ex.getMessage());
        dto.setDetail("Check the prod id. It probably doesn't exist.");
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleCategoryNotFoundException(CategoryNotFoundException ex){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMessage(ex.getMessage());
        dto.setDetail("Check the category id. It probably doesn't exist.");
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryAlreadyExists.class)
    public ResponseEntity<ExceptionDTO> handleCategoryAlreadyExists(CategoryAlreadyExists ex){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMessage(ex.getMessage());
        dto.setDetail("A category with this title already exists.");
        return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
    }
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<ExceptionDTO> handleNoHandlerFoundException(NoHandlerFoundException ex){
//        ExceptionDTO dto = new ExceptionDTO();
//        dto.setMessage(ex.getMessage());
//        dto.setDetail("Unknown Endpoint");
//        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
//    }
}

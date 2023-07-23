package com.coyoteforms.example.configuration;

import com.coyoteforms.example.validation.ValidationErrorResponse;
import com.coyoteforms.example.validation.ValidationErrorResponseItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class PublishValidationResultAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ValidationErrorResponse> handleValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        List<ValidationErrorResponseItem> validationErrors = ex.getFieldErrors().stream()
                .map(error -> ValidationErrorResponseItem.builder()
                        .fieldId(error.getField())
                        .reason(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ValidationErrorResponse
                        .builder()
                        .invalidFields(validationErrors)
                        .build()
                );
    }


}
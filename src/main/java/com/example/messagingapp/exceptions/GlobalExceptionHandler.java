package com.example.messagingapp.exceptions;

import com.example.messagingapp.controller.model.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<UserResponse> handlerIllegalArgumentException(IllegalArgumentException ex) {
        String message = ex.getMessage();
        UserResponse response = UserResponse.builder().message(message).success(true).httpStatus(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package com.example.turntable.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedUsernameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedUsernameException(DuplicatedUsernameException ex) {
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(),ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}

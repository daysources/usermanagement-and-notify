package com.daysources.thirdchallenge.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UnavailableUsernameException.class)
    public ResponseEntity<?> unavailableUsernameException(UnavailableUsernameException ex){
        return new ResponseEntity<>(new ErrorMessage("USERNAME UNAVAILABLE", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidZipcodeException.class)
    public ResponseEntity<?> invalidZipcodeException(InvalidZipcodeException ex){
        return new ResponseEntity<>(new ErrorMessage("ADDRESS FETCH ERROR", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> invalidCredentialsException(InvalidCredentialsException ex){
        return new ResponseEntity<>(new ErrorMessage("INVALID CREDENTIALS", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Getter @Setter
    static class ErrorMessage {
        private String error;
        private String message;

        public ErrorMessage(String error, String message){
            this.error=error;
            this.message=message;
        }
    }

}

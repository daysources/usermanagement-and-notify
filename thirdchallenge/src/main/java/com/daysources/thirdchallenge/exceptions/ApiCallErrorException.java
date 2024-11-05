package com.daysources.thirdchallenge.exceptions;

public class ApiCallErrorException extends RuntimeException{

    public ApiCallErrorException(String message) {
        super(message);
    }
}

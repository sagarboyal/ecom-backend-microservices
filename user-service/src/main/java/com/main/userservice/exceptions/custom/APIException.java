package com.main.userservice.exceptions.custom;


public class APIException extends RuntimeException {
    public APIException() {}
    public APIException(String message) {
        super(message);
    }
}

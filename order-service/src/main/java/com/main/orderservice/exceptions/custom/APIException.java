package com.main.orderservice.exceptions.custom;


public class APIException extends RuntimeException {
    public APIException() {}
    public APIException(String message) {
        super(message);
    }
}

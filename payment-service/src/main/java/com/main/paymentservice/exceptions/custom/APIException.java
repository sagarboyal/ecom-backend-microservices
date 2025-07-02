package com.main.paymentservice.exceptions.custom;


public class APIException extends RuntimeException {
    public APIException() {}
    public APIException(String message) {
        super(message);
    }
}

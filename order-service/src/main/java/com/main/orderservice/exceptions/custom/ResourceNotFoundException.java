package com.main.orderservice.exceptions.custom;

public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String field;

    public ResourceNotFoundException() {
        super("Requested resource was not found.");
    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("The %s with %s '%s' was not found.", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("The %s with %s '%d' was not found.", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
    }
}

package com.epam.ecommerce.exceptions;

// Extending it to runtime exception
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

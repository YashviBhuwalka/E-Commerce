package com.epam.ecommerce.exceptions;

// Extending it to runtime exception
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}

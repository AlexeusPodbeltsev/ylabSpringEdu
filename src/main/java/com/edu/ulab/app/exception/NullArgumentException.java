package com.edu.ulab.app.exception;

/**
 * Exception is thrown when argument is null
 */
public class NullArgumentException extends RuntimeException{
    public NullArgumentException(String message) {
        super(message);
    }
}

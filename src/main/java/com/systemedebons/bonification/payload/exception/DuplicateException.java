package com.systemedebons.bonification.payload.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super("Duplicate entry: " + message);
    }
}

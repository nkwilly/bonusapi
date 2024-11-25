package com.systemedebons.bonification.payload.exception;

public class AccessClientException extends RuntimeException {
    public AccessClientException() {
        super ("You can only manage your own clients");
    }
}

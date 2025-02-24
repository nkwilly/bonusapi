package com.systemedebons.bonification.payload.exception;

public class AccessTransactionException extends RuntimeException {
    public AccessTransactionException() {
        super("You can only access transactions of your own clients");
    }
}

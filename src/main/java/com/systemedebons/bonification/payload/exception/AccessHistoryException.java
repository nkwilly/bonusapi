package com.systemedebons.bonification.payload.exception;

public class AccessHistoryException extends RuntimeException{
    public AccessHistoryException() {
        super("You can only access historique of your own client");
    }
}

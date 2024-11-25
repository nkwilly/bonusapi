package com.systemedebons.bonification.payload.exception;

public class AccessHistoriqueException extends RuntimeException{
    public AccessHistoriqueException() {
        super("You can only access historique of your own client");
    }
}

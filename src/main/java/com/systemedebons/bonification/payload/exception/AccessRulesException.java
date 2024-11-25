package com.systemedebons.bonification.payload.exception;

public class AccessRulesException extends RuntimeException{
    public AccessRulesException() {
        super("You can only manage your own rules");
    }
}

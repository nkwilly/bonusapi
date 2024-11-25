package com.systemedebons.bonification.payload.exception;

public class AccessRewardException extends RuntimeException{
    public AccessRewardException() {
        super("You can only access rewards of your own clients");
    }
}

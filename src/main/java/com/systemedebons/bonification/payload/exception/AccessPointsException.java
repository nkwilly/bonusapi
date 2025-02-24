package com.systemedebons.bonification.payload.exception;

import org.springframework.security.access.AccessDeniedException;

public class AccessPointsException extends AccessDeniedException {
    public AccessPointsException() {
        super("You can only manage points of your own client");
    }
}

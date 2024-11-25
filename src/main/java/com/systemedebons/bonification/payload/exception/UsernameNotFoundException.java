package com.systemedebons.bonification.payload.exception;

public class UsernameNotFoundException extends org.springframework.security.core.userdetails.UsernameNotFoundException {
    public UsernameNotFoundException() {
        super("User not found");
    }
}

package com.systemedebons.bonification.payload.exception;

public class EntityNotFound extends RuntimeException {
    public EntityNotFound(Object entity) {
        super("Entity not found: " + entity);
    }
}

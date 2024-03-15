package com.riquelme.springbootcrudhibernaterestful.exceptions;

public class RoleServiceException extends RuntimeException {
    public RoleServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
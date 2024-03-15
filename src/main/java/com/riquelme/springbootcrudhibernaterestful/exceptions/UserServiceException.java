package com.riquelme.springbootcrudhibernaterestful.exceptions;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
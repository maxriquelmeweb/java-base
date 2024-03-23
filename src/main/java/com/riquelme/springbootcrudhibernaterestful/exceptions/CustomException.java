package com.riquelme.springbootcrudhibernaterestful.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomException extends RuntimeException {
    private HttpStatus statusCode;
    private String keyMessage;
    private Throwable throwable;

    public CustomException(String keyMessage) {
        this.keyMessage = keyMessage;
    }

    public CustomException(String keyMessage, Throwable throwable) {
        this(keyMessage);
        this.throwable = throwable;
        this.statusCode = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getKeyMessage() {
        return keyMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}

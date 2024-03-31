package com.riquelme.javabase.exceptions.jwt;

import com.riquelme.javabase.exceptions.BaseException;

public class CustomDeserializationException extends BaseException{
    public CustomDeserializationException(String messageKey) {
        super(messageKey);
    }
    public CustomDeserializationException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}

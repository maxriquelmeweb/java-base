package com.riquelme.springbootcrudhibernaterestful.exceptions.jwt;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class CustomDeserializationException extends BaseException{
    public CustomDeserializationException(String messageKey) {
        super(messageKey);
    }
    public CustomDeserializationException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }
}

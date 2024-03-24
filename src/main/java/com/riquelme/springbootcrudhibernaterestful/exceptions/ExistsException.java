package com.riquelme.springbootcrudhibernaterestful.exceptions;

public class ExistsException extends BaseException {
    public ExistsException(String messageKey) {
        super(messageKey);
    }
}

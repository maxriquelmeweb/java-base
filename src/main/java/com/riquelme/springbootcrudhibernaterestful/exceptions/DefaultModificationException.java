package com.riquelme.springbootcrudhibernaterestful.exceptions;

public class DefaultModificationException extends BaseException {
    public DefaultModificationException(String messageKey) {
        super(messageKey);
    }
}

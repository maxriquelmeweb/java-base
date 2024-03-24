package com.riquelme.springbootcrudhibernaterestful.exceptions;

public class NotFoundException extends BaseException {

    public NotFoundException(String messageKey) {
        super(messageKey);
    }

}

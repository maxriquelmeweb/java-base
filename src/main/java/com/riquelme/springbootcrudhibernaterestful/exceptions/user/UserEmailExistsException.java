package com.riquelme.springbootcrudhibernaterestful.exceptions.user;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class UserEmailExistsException extends BaseException {
    public UserEmailExistsException(String messageKey) {
        super(messageKey);
    }
}

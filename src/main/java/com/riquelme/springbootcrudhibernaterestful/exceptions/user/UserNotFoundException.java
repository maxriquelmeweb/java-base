package com.riquelme.springbootcrudhibernaterestful.exceptions.user;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String messageKey) {
        super(messageKey);
    }

}

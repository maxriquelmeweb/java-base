package com.riquelme.springbootcrudhibernaterestful.exceptions.user;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class UserRoleNotFoundException extends BaseException {

    public UserRoleNotFoundException(String messageKey) {
        super(messageKey);
    }

}
